package com.udacity.google.popularmovies;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.udacity.google.popularmovies.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {

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
