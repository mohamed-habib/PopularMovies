package com.udacity.google.popularmovies.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.udacity.google.popularmovies.DetailActivity
import com.udacity.google.popularmovies.util.FavouriteDataSource
import com.udacity.google.popularmovies.util.CustomGridAdapter
import com.udacity.google.popularmovies.util.Movie
import com.udacity.google.popularmovies.R
import com.udacity.google.popularmovies.repository.remote.MoviesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import java.util.ArrayList

/**
 * Created by Dell on 9/29/2015.
 */
class MainFragment : Fragment() {
    private lateinit var gridView: GridView
    private lateinit var rootView: View
    private lateinit var popularMovies: ArrayList<Movie>
    private lateinit var poster: String
    private lateinit var queue: RequestQueue
    private val POP_DESC = "popularity.desc"
    private var VOTE_DSEC = "vote_average.desc"
    private var FAV = "favourite"
    private var API_KEY: String? = null
    private var TAG = "APIDebug"
    private var sort_by: String = POP_DESC
    lateinit var adapter: CustomGridAdapter
    lateinit var sp: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.main_fragment, container, false)

        API_KEY = activity?.resources?.getString(R.string.api_key)
        gridView = rootView.findViewById<View>(R.id.gridView) as GridView
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
                val intent = Intent(activity, DetailActivity::class.java)
                startActivity(intent)
            }
        }

        return rootView
    }

    private fun jsonObjectRequest(sort_by: String) {

        // http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=4f92be250f018aff8f3a2b5c3864aecd
        popularMovies = ArrayList()

        val moviesRepository = MoviesRepository()
        moviesRepository.getMovies(sort_by, API_KEY!!).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.movieList.forEach { movie ->
                        movie.poster_path = "https://image.tmdb.org/t/p/w185/" + movie.poster_path;
                        popularMovies.add(movie)
                    }

                    adapter = CustomGridAdapter(activity!!, popularMovies)
                    gridView.adapter = adapter
                }, {
                    Toast.makeText(activity, "Sorry some thing happened while retrieving data, please try again", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, it.message)
                })
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
        val dataSource = FavouriteDataSource(activity!!)
        dataSource.open()
        val favoriteMovies = dataSource.favouriteMovies
        dataSource.close()

        if (favoriteMovies.size == 0) {
            Toast.makeText(activity, "You don't have any favourite movies yet, mark a movie as favourite first", Toast.LENGTH_LONG).show()
        } else {
            val adapter = CustomGridAdapter(activity!!, favoriteMovies)
            gridView.adapter = adapter
        }
    }

    companion object {
        lateinit var clickedMovie: Movie
    }

}
