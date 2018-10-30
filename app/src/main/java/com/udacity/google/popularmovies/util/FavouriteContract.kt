package com.udacity.google.popularmovies.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.UriMatcher
import android.net.Uri
import android.provider.BaseColumns

import java.net.URI

/**
 * Created by Dell on 10/12/2015.
 */


public object FavouriteContract {
    val PATH_FAVOURITE = "favourite"

    public val CONTENT_AUTHORITY: String = "com.udacity.google.popularmovies.app"

    val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")


    class FavouriteEntry : BaseColumns {
        companion object {

            val COLUMN_ID = "id"
            val COLUMN_MOVIE_ID = "movie_id"
            val COLUMN_MOVIE_POSTER = "movie_poster"
            val COLUMN_MOVIE_TITLE = "movie_title"
            val COLUMN_MOVIE_RELEASE_DATE = "movie_release_date"
            val COLUMN_MOVIE_VOTE_AVERAGE = "movie_vote_average"
            val COLUMN_MOVIE_OVERVIEW = "movie_overview"


            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build()

            val CONTENT_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE
            val CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE

            val TABLE_NAME = "favourite"

            fun buildFavouriteUri(id: Long): Uri {
                return ContentUris.withAppendedId(CONTENT_URI, id)
            }
        }


    }
}
