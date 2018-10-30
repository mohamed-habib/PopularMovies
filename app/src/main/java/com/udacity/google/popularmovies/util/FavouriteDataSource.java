package com.udacity.google.popularmovies.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.udacity.google.popularmovies.MainActivity;

import java.util.ArrayList;

/**
 * Created by Dell on 10/9/2015.
 */
public class FavouriteDataSource {
    SQLiteOpenHelper openHelper;
    public static SQLiteDatabase database;

    public FavouriteDataSource(Context context) {
        openHelper = new FavouriteOpenHelper(context);
    }

    public void open() {

        database = openHelper.getWritableDatabase();
    }

    public void close() {
        openHelper.close();
    }


    public void create(Movie movie) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(FavouriteOpenHelper.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(FavouriteOpenHelper.COLUMN_MOVIE_POSTER, movie.getPosterPath());
        contentValues.put(FavouriteOpenHelper.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        contentValues.put(FavouriteOpenHelper.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavouriteOpenHelper.COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(FavouriteOpenHelper.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());

        ContentValues values = new ContentValues();
        values.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID, "1");


        database.insert(FavouriteOpenHelper.TABLE_FAVOURITE, null, contentValues);

    }

    public void delete(int movieID) {
        String whereClause = FavouriteOpenHelper.COLUMN_MOVIE_ID + " = " + movieID;

        database.delete(FavouriteOpenHelper.TABLE_FAVOURITE, whereClause, null);
    }

    public ArrayList<Movie> getFavouriteMovies() {


        ArrayList<Movie> movies = new ArrayList<Movie>();

        Cursor cursor = database.query(FavouriteOpenHelper.TABLE_FAVOURITE, FavouriteOpenHelper.ALL_COLUMNS, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                Movie movie = new Movie();
                movie.setId(cursor.getInt(cursor
                        .getColumnIndex(FavouriteOpenHelper.COLUMN_MOVIE_ID)));
                movie.setPosterPath(cursor.getString(cursor
                        .getColumnIndex(FavouriteOpenHelper.COLUMN_MOVIE_POSTER)));
                movie.setReleaseDate(cursor.getString(cursor
                        .getColumnIndex(FavouriteOpenHelper.COLUMN_MOVIE_RELEASE_DATE)));
                ;
                movie.setOverview(cursor.getString(cursor
                        .getColumnIndex(FavouriteOpenHelper.COLUMN_MOVIE_OVERVIEW)));
                movie.setTitle(cursor.getString(cursor
                        .getColumnIndex(FavouriteOpenHelper.COLUMN_MOVIE_TITLE)));
                movie.setVoteAverage(cursor.getString(cursor
                        .getColumnIndex(FavouriteOpenHelper.COLUMN_MOVIE_VOTE_AVERAGE)));


                movies.add(movie);
            }
        }
        return movies;
    }

    public boolean isFavouriteMovie(int movieId) {

        String selection = FavouriteOpenHelper.COLUMN_MOVIE_ID + "=" + movieId;

        Cursor cursor = database.query(FavouriteOpenHelper.TABLE_FAVOURITE, FavouriteOpenHelper.ALL_COLUMNS, selection, null, null, null, null);

        if (cursor.getCount() > 0)
            return true;
        else
            return false;


    }


}
