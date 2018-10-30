package com.udacity.google.popularmovies.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.support.v7.widget.ShareActionProvider
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.udacity.google.popularmovies.util.FavouriteDataSource
import com.udacity.google.popularmovies.R

import org.json.JSONArray
import org.json.JSONException

import java.util.ArrayList

class DetailFragment : Fragment() {

    private var clickedMovieId: Int = 0
    private lateinit var reviewsLV: ListView
    private lateinit var trailersLv: ListView
    private lateinit var queue: RequestQueue
    private lateinit var trailersNames: ArrayList<String>
    private var trailersKeys: ArrayList<String>? = null
    private var mShareActionProvider: ShareActionProvider? = null
    internal lateinit var shareIntent: Intent

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.detail_fragment, null, false)
        val favButton = rootView.findViewById<View>(R.id.FavtoggleButton) as ToggleButton
        clickedMovieId = MainFragment.clickedMovie.id
        val movieTitle = rootView.findViewById<View>(R.id.movie_title) as TextView
        movieTitle.text = MainFragment.clickedMovie.title
        val moviePoster = rootView.findViewById<View>(R.id.movie_poster) as ImageView
        Picasso.with(activity).load(MainFragment.clickedMovie.poster_path).resize(300, 300).centerInside().into(moviePoster)
        Log.d("DetailDebug", MainFragment.clickedMovie.poster_path)
        val movieDate = rootView.findViewById<View>(R.id.movie_date) as TextView
        movieDate.text = MainFragment.clickedMovie.release_date

        val movieVoteAverage = rootView.findViewById<View>(R.id.movie_vote_average) as TextView
        movieVoteAverage.text = MainFragment.clickedMovie.vote_average

        val movieOverView = rootView.findViewById<View>(R.id.movie_overview) as TextView
        movieOverView.text = MainFragment.clickedMovie.overview

        favButton.setOnCheckedChangeListener { compoundButton, b ->
            val favouriteDataSource = FavouriteDataSource(activity!!)
            favouriteDataSource.open()

            if (favButton.text == "MARK AS A FAVOURITE") {
                //                    save clicked movie id and poster link to db
                favouriteDataSource.create(MainFragment.clickedMovie)
                Toast.makeText(activity, "Marked as FAVOURITE", Toast.LENGTH_LONG).show()
            } else {
                favouriteDataSource.delete(MainFragment.clickedMovie.id)
                Toast.makeText(activity, "Marked as not favourite", Toast.LENGTH_LONG).show()
            }

            favouriteDataSource.close()
        }

        trailersLv = rootView.findViewById<View>(R.id.trailers_list_view) as ListView

        trailersLv.setOnTouchListener { v, event ->
            // Setting on Touch Listener for handling the touch inside ScrollView
            // Disallow the touch request for parent scroll on touch of child view
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }


        queue = Volley.newRequestQueue(activity!!)


        trailersNames = ArrayList()
        trailersKeys = ArrayList()

        var builder = Uri.Builder()

        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + clickedMovieId)
                .appendPath("videos")
                .appendQueryParameter("api_key", activity!!.resources.getString(R.string.api_key))

        //http://api.themoviedb.org/3/movie/49026/videos?api_key=4f92be250f018aff8f3a2b5c3864aecd
        var myUrl = builder.build().toString()

        Log.d("VolleyDebug", myUrl)
        val trailersRequest = JsonObjectRequest(Request.Method.GET, myUrl, null, Response.Listener { response ->
            var resultJsonArray: JSONArray? = null
            try {

                resultJsonArray = response.getJSONArray("results")


                for (i in 0 until resultJsonArray!!.length()) {

                    val jsonObject = resultJsonArray.getJSONObject(i)

                    val name = jsonObject.getString("name")
                    val key = jsonObject.getString("key")

                    trailersNames.add(name)
                    trailersKeys!!.add(key)
                    val adapter = CustomListAdapter(activity!!, trailersNames)

                    trailersLv.adapter = adapter

                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },
                Response.ErrorListener { Toast.makeText(activity, "Sorry some thing happened while retrieving trailers, please try again", Toast.LENGTH_LONG).show() }
        )

        queue.add(trailersRequest)


        trailersLv.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val clickedKey = trailersKeys!![i]

            val url = "https://www.youtube.com/watch?v=$clickedKey"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        reviewsLV = rootView.findViewById<View>(R.id.reviews_list_view) as ListView

        val reviewsArrayList = ArrayList<String>()


        builder = Uri.Builder()

        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + clickedMovieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", activity!!.resources.getString(R.string.api_key))

        //http://api.themoviedb.org/3/movie/49026/reviews?api_key=4f92be250f018aff8f3a2b5c3864aecd

        myUrl = builder.build().toString()


        val reviewsRequest = JsonObjectRequest(Request.Method.GET, myUrl, null, Response.Listener { response ->
            try {
                val resultsJsonArray = response.getJSONArray("results")


                for (i in 0 until resultsJsonArray.length()) {

                    val resultJsonObj = resultsJsonArray.getJSONObject(i)

                    val author = resultJsonObj.getString("author")

                    val content = resultJsonObj.getString("content")

                    //TODO: author with content !
                    reviewsArrayList.add(author + ": " + System.getProperty("line.separator") + System.getProperty("line.separator") + content)

                    Log.d("DetailDebug", "$author: /r/n$content")

                    val reviewsAdapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, reviewsArrayList)
                    reviewsLV.adapter = reviewsAdapter

                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },
                Response.ErrorListener { }
        )

        queue.add(reviewsRequest)


        reviewsLV.setOnTouchListener { v, event ->
            // Setting on Touch Listener for handling the touch inside ScrollView
            // Disallow the touch request for parent scroll on touch of child view
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }


        val favouriteDataSource = FavouriteDataSource(activity!!)
        favouriteDataSource.open()
        if (favouriteDataSource.isFavouriteMovie(clickedMovieId)) {

            favButton.text = "FAVOURITE"
        }


        return rootView
    }

    inner class CustomListAdapter(//ArrayList<String> images ;
            internal var context: Activity, internal var stringArrayList: ArrayList<String>) : ArrayAdapter<Any>(context, R.layout.grid_view_item) {

        override fun getCount(): Int {
            return stringArrayList.size
        }

        override fun getItem(position: Int): Any? {
            return stringArrayList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            val inflater = context.layoutInflater

            val rowView = inflater.inflate(R.layout.list_view_item, null, true)

            val tv = rowView.findViewById<View>(R.id.list_item_text_view) as TextView

            tv.text = stringArrayList[position]

            return rowView


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        inflater!!.inflate(R.menu.menu_detail, menu)


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.menu_item_share) {
            mShareActionProvider = MenuItemCompat.getActionProvider(item) as ShareActionProvider

            if (trailersKeys != null && trailersKeys!!.size > 0) {
                val url = "https://www.youtube.com/watch?v=" + trailersKeys!![0]

                shareIntent = Intent(android.content.Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_STREAM, url)
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            }
            if (mShareActionProvider != null) {
                mShareActionProvider!!.setShareIntent(shareIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }


}
