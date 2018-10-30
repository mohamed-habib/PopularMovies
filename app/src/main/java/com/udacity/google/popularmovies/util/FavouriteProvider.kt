package com.udacity.google.popularmovies.util

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

import java.util.HashMap

/**
 * Created by Dell on 10/12/2015.
 */

class FavouriteProvider : ContentProvider() {
    private val mOpenHelper: FavouriteOpenHelper? = null
    internal lateinit var dataSource: FavouriteDataSource

    override fun onCreate(): Boolean {
        dataSource = FavouriteDataSource(context!!)

        dataSource.open()


        return true
    }


    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {


        val qb = SQLiteQueryBuilder()
        val dataSource = FavouriteDataSource(context!!)

        qb.tables = FavouriteContract.CONTENT_AUTHORITY

        val retCursor: Cursor
        when (uriMatcher.match(uri)) {

            uriCode -> qb.setProjectionMap(values)
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }

        val c = qb.query(dataSource.openHelper.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder)

        c.setNotificationUri(context!!.contentResolver, uri)
        return c
    }

    override fun getType(uri: Uri): String? {
        when (uriMatcher.match(uri)) {
            uriCode -> return FavouriteContract.FavouriteEntry.CONTENT_TYPE
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {

        var count = 0
        when (uriMatcher.match(uri)) {
            uriCode -> count = FavouriteDataSource.database.delete(FavouriteContract.PATH_FAVOURITE, s, strings)
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int {
        var count = 0
        when (uriMatcher.match(uri)) {
            uriCode -> count = FavouriteDataSource.database.update(FavouriteContract.PATH_FAVOURITE, contentValues, s, strings)
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    companion object {


        internal val uriCode = 1
        internal val uriMatcher: UriMatcher
        private val values: HashMap<String, String>? = null

        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(FavouriteContract.CONTENT_AUTHORITY, "favourite", uriCode)
            uriMatcher.addURI(FavouriteContract.CONTENT_AUTHORITY, "favourite/*", uriCode)
        }
    }


}
