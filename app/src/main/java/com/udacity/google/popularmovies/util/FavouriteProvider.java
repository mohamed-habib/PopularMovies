package com.udacity.google.popularmovies.util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by Dell on 10/12/2015.
 */

public class FavouriteProvider extends ContentProvider {
    private FavouriteOpenHelper mOpenHelper;


    static final int uriCode = 1;
    static final UriMatcher uriMatcher;
    private static HashMap<String, String> values;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavouriteContract.CONTENT_AUTHORITY, "favourite", uriCode);
        uriMatcher.addURI(FavouriteContract.CONTENT_AUTHORITY, "favourite/*", uriCode);
    }


    FavouriteDataSource dataSource;

    @Override
    public boolean onCreate() {
        dataSource = new FavouriteDataSource(getContext());

        dataSource.open();


        return true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {


                SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                FavouriteDataSource dataSource = new FavouriteDataSource(getContext());

                qb.setTables(FavouriteContract.CONTENT_AUTHORITY);

                Cursor retCursor;
                switch (uriMatcher.match(uri)) {

                    case uriCode:
                        qb.setProjectionMap(values);
                        break;
                    default:
                        throw new UnsupportedOperationException("Unknown uri: " + uri);
                        }

        Cursor c = qb.query(dataSource.openHelper.getWritableDatabase(), projection, selection, selectionArgs, null,
                null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case uriCode:
               return FavouriteContract.FavouriteEntry.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {

        int count = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode:
                count = FavouriteDataSource.database.delete(FavouriteContract.PATH_FAVOURITE, s, strings);

                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode:
                count = FavouriteDataSource.database.update(FavouriteContract.PATH_FAVOURITE, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }






}
