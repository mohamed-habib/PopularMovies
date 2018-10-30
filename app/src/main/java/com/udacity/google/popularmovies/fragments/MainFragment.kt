package com.udacity.google.popularmovies.fragments

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.udacity.google.popularmovies.DetailActivity
import com.udacity.google.popularmovies.util.FavouriteContract
import com.udacity.google.popularmovies.util.FavouriteDataSource
import com.udacity.google.popularmovies.util.CustomGridAdapter
import com.udacity.google.popularmovies.util.FavouriteOpenHelper
import com.udacity.google.popularmovies.util.FavouriteProvider
import com.udacity.google.popularmovies.util.Movie
import com.udacity.google.popularmovies.R

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.prefs.PreferenceChangeEvent

/**
 * Created by Dell on 9/29/2015.
 */
class MainFragment : Fragment() {
    lateinit var gridView: GridView
    internal var rootView: View? = null
    lateinit var popularMovies: ArrayList<Movie>
    lateinit var popularMoviePosters: ArrayList<String>
    lateinit var poster: String
    lateinit var queue: RequestQueue

    private var POP_DESC = "popularity.desc"
    private var VOTE_DSEC = "vote_average.desc"
    private var FAV = "favourite"
    private var API_KEY: String? = null
    private var TAG = "VolleyDebug"
    private var sort_by: String = POP_DESC
    lateinit var adapter: CustomGridAdapter
    lateinit var sp: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.main_fragment, container, false)

        API_KEY = activity?.resources?.getString(R.string.api_key)
        gridView = rootView?.findViewById<View>(R.id.gridView) as GridView
        queue = Volley.newRequestQueue(activity)
        sp = PreferenceManager.getDefaultSharedPreferences(activity!!.applicationContext)

        sort_by = sp.getString("sort_by", POP_DESC)
        if (sort_by == FAV)
            getFavouriteFromDB()
        else {
            jsonObjectRequest(sort_by)
        }

        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, view, _, _ ->
            val layout = view as LinearLayout
            val clickedItem = layout.findViewById<View>(R.id.imageView) as ImageView
            clickedMovie = clickedItem.tag as Movie

            if (activity!!.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE) {

                //if tablet, add detailFragment
                val transaction = activity!!.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container_detail, DetailFragment())
                transaction.commit()
            } else {
                //if mobile device, open DetailActivity
                val `in` = Intent(activity, DetailActivity::class.java)
                //in.putExtra("movie", movie); ???!
                startActivity(`in`)
            }
        }

        return rootView
    }

    private fun jsonObjectRequest(sort_by: String) {
        val builder = Uri.Builder()
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by", sort_by)
                .appendQueryParameter("api_key", API_KEY)

        // http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=4f92be250f018aff8f3a2b5c3864aecd

        val myUrl = builder.build().toString()
        Log.d("popularMovies", myUrl)
        popularMoviePosters = ArrayList()
        popularMovies = ArrayList()
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, myUrl, null, Response.Listener { response ->
            try {

                val jsonArrayResult = response.getJSONArray("results")

                for (i in 0 until jsonArrayResult.length()) {
                    val movie = Movie()
                    val jsonObjectMovie = jsonArrayResult.getJSONObject(i)
                    movie.id = jsonObjectMovie.getInt("id")
                    movie.overview = jsonObjectMovie.getString("overview")
                    movie.posterPath = "https://image.tmdb.org/t/p/w185/" + jsonObjectMovie.getString("poster_path")
                    movie.releaseDate = jsonObjectMovie.getString("release_date")
                    movie.title = jsonObjectMovie.getString("title")
                    movie.voteAverage = jsonObjectMovie.getString("vote_average")
                    popularMovies.add(movie)

                    poster = "https://image.tmdb.org/t/p/w185/"
                    poster += jsonObjectMovie.getString("poster_path")
                    popularMoviePosters.add(poster)
                    Log.d(TAG, popularMoviePosters[i])
                }
                adapter = CustomGridAdapter(activity, popularMovies)
                gridView.adapter = adapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(activity, "Sorry some thing happened while retrieving data, please try again", Toast.LENGTH_SHORT).show()
            Log.e(TAG, error.message)
        })

        queue.add(jsonObjectRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater!!.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item!!.itemId
        if (id == R.id.action_most_popular) {
            sp.edit().putString("sort_by", POP_DESC).apply()
            jsonObjectRequest(POP_DESC)
            return true
        }
        if (id == R.id.action_highest_rated) {
            sp.edit().putString("sort_by", VOTE_DSEC).apply()
            jsonObjectRequest(VOTE_DSEC)
            return true
        }
        if (id == R.id.action_favourite) {
            sp.edit().putString("sort_by", "favourite").apply()
            getFavouriteFromDB()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getFavouriteFromDB() {
        //retrieve movies from db and show them on grid view
        val dataSource = FavouriteDataSource(activity)
        dataSource.open()
        val favoriteMovies = dataSource.favouriteMovies
        dataSource.close()

        if (favoriteMovies.size == 0) {
            Toast.makeText(activity, "You don't have any favourite movies yet, mark a movie as favourite first", Toast.LENGTH_LONG).show()
        } else {
            val adapter = CustomGridAdapter(activity, favoriteMovies)
            gridView.adapter = adapter
        }
    }

    companion object {
        lateinit var clickedMovie: Movie
    }

}
