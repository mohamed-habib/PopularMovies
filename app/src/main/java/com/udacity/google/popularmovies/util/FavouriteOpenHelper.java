package com.udacity.google.popularmovies.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dell on 10/9/2015.
 */
public class FavouriteOpenHelper extends SQLiteOpenHelper {


    private static final String DBName =  "Favourite.db";
    private static final int DATABASE_VERSION = 1;

    public  static final String TABLE_FAVOURITE = "Favourite";
    public  static final String COLUMN_ID = "id";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_MOVIE_POSTER = "movie_poster";
    public static final String COLUMN_MOVIE_TITLE = "movie_title";
    public static final String COLUMN_MOVIE_RELEASE_DATE= "movie_release_date";
    public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movie_vote_average";
    public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";

    public static final  String[] ALL_COLUMNS = {FavouriteOpenHelper.COLUMN_MOVIE_ID ,FavouriteOpenHelper.COLUMN_MOVIE_POSTER ,FavouriteOpenHelper.COLUMN_MOVIE_TITLE,FavouriteOpenHelper.COLUMN_MOVIE_RELEASE_DATE,FavouriteOpenHelper.COLUMN_MOVIE_VOTE_AVERAGE,FavouriteOpenHelper.COLUMN_MOVIE_OVERVIEW};


    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_FAVOURITE + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_MOVIE_ID + " INTEGER, " +
            COLUMN_MOVIE_POSTER + " TEXT, " +
            COLUMN_MOVIE_OVERVIEW + " TEXT, " +
            COLUMN_MOVIE_RELEASE_DATE + " TEXT, " +
            COLUMN_MOVIE_TITLE + " TEXT, " +
            COLUMN_MOVIE_VOTE_AVERAGE + " TEXT " +
            ")" ;



    public FavouriteOpenHelper(Context context) {
        super(context, DBName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE);
        onCreate(sqLiteDatabase);
    }
}



