package com.udacity.google.popularmovies.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Created by Dell on 10/12/2015.
 */


public class FavouriteContract {
    public static final String PATH_FAVOURITE= "favourite";

    public static final String CONTENT_AUTHORITY = "com.udacity.google.popularmovies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);





    public static final class FavouriteEntry implements BaseColumns{

        public  static final String COLUMN_ID = "id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_POSTER = "movie_poster";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_RELEASE_DATE= "movie_release_date";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movie_vote_average";
        public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";



        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();

        public static final String CONTENT_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;
        public static final String CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;

        public static final String TABLE_NAME = "favourite";

        public static Uri buildFavouriteUri(long id) {
                       return ContentUris.withAppendedId(CONTENT_URI, id);
                   }




    }
}
