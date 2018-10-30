package com.udacity.google.popularmovies.util

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

import com.squareup.picasso.Picasso
import com.udacity.google.popularmovies.R

import java.util.ArrayList

/**
 * Created by Dell on 8/29/2015.
 */
class CustomGridAdapter(//ArrayList<String> images ;
        internal var context: Activity, internal var movies: ArrayList<Movie>) : ArrayAdapter<Any>(context, R.layout.grid_view_item) {

    override fun getCount(): Int {
        return movies.size
    }

    override fun getItem(position: Int): Any? {
        return movies[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater

        val rowView = inflater.inflate(R.layout.grid_view_item, null, true)

        val imageView = rowView.findViewById<View>(R.id.imageView) as ImageView

        val movie = getItem(position) as Movie?

        imageView.tag = movie

        val url = movie!!.posterPath

        Log.d("PicassoDebug", position.toString() + " " + url)
        Picasso.with(context).load(url).resize(500, 500).centerInside().into(imageView)
        return rowView
    }
}
