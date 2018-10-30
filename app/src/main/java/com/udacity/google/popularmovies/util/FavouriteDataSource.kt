package com.udacity.google.popularmovies.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.view.View
import android.widget.Toast

import com.udacity.google.popularmovies.MainActivity

import java.util.ArrayList

/**
 * Created by Dell on 10/9/2015.
 */
class FavouriteDataSource(context: Context) {
    internal var openHelper: SQLiteOpenHelper = FavouriteOpenHelper(context)

    val favouriteMovies: ArrayList<Movie>
        get() {
            val movies = ArrayList<Movie>()

            val cursor = database.query(FavouriteOpenHelper.TABLE_FAVOURITE, FavouriteOpenHelper.ALL_COLUMNS, null, null, null, null, null)

            if (cursor.count > 0) {
                while (cursor.moveToNext()) {

                    val movie = Movie()
                    movie.id = cursor.getInt(cursor
                            .getColumnIndex(FavouriteOpenHelper.COLUMN_MOVIE_ID))
                    movie.posterPath = cursor.getString(cursor
                            .getColumnIndex(FavouriteOpenHelper.COLUMN_MOVIE_POSTER))
                    movie.releaseDate = cursor.getString(cursor
                            .getColumnIndex(FavouriteOpenHelper.COLUMN_MOVIE_RELEASE_DATE))
                    movie.overview = cursor.getString(cursor
                            .getColumnIndex(FavouriteOpenHelper.COLUMN_MOVIE_OVERVIEW))
                    movie.title = cursor.getString(cursor
                            .getColumnIndex(FavouriteOpenHelper.COLUMN_MOVIE_TITLE))
                    movie.voteAverage = cursor.getString(cursor
                            .getColumnIndex(FavouriteOpenHelper.COLUMN_MOVIE_VOTE_AVERAGE))


                    movies.add(movie)
                }
            }
            return movies
        }

    fun open() {

        database = openHelper.writableDatabase
    }

    fun close() {
        openHelper.close()
    }


    fun create(movie: Movie) {

        val contentValues = ContentValues()

        contentValues.put(FavouriteOpenHelper.COLUMN_MOVIE_ID, movie.id)
        contentValues.put(FavouriteOpenHelper.COLUMN_MOVIE_POSTER, movie.posterPath)
        contentValues.put(FavouriteOpenHelper.COLUMN_MOVIE_OVERVIEW, movie.overview)
        contentValues.put(FavouriteOpenHelper.COLUMN_MOVIE_RELEASE_DATE, movie.releaseDate)
        contentValues.put(FavouriteOpenHelper.COLUMN_MOVIE_TITLE, movie.title)
        contentValues.put(FavouriteOpenHelper.COLUMN_MOVIE_VOTE_AVERAGE, movie.voteAverage)

        val values = ContentValues()
        values.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID, "1")


        database.insert(FavouriteOpenHelper.TABLE_FAVOURITE, null, contentValues)

    }

    fun delete(movieID: Int) {
        val whereClause = FavouriteOpenHelper.COLUMN_MOVIE_ID + " = " + movieID

        database.delete(FavouriteOpenHelper.TABLE_FAVOURITE, whereClause, null)
    }

    fun isFavouriteMovie(movieId: Int): Boolean {

        val selection = FavouriteOpenHelper.COLUMN_MOVIE_ID + "=" + movieId
        val cursor = database.query(FavouriteOpenHelper.TABLE_FAVOURITE, FavouriteOpenHelper.ALL_COLUMNS, selection, null, null, null, null)
        return cursor.count > 0
    }

    companion object {
        lateinit var database: SQLiteDatabase
    }


}
