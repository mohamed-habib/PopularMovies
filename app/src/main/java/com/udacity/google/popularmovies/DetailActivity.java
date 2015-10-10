package com.udacity.google.popularmovies;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.google.popularmovies.fragments.DetailFragment;
import com.udacity.google.popularmovies.util.Movie;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        Intent in = getIntent();
//
//        Movie movie = in.getParcelableExtra("movie");
//
//        Toast.makeText(this,movie.getReleaseDate(),Toast.LENGTH_LONG).show();


        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.detail_container, new DetailFragment());

        transaction.commit();


    }


}
