package com.udacity.google.popularmovies.util;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.google.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by Dell on 8/29/2015.
 */
public class CustomGridAdapter extends ArrayAdapter {

    //ArrayList<String> images ;
    Activity context;

    ArrayList<Movie> movies;
    public CustomGridAdapter(Activity context,  ArrayList<Movie> resources) {
        super(context, R.layout.grid_view_item);

        movies = resources;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) { return movies.get(position);}

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.grid_view_item, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);

        Movie movie =(Movie) getItem(position);

        imageView.setTag(movie);

        String url = movie.getPosterPath();

        //String url =(String) getItem(position);

        Log.d("PicassoDebug", position + " " + url);
        Picasso.with(context).load(url).resize(500, 500).centerInside().into(imageView);


//        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

//        ViewGroup.LayoutParams layoutParams = new GridView.LayoutParams(70, 70);
//
//        imageView.setLayoutParams(layoutParams );

        return rowView;


    }
}
