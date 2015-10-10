package com.udacity.google.popularmovies;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.google.popularmovies.fragments.MainFragment;


public class MainActivity extends ActionBarActivity {

    public static Context context= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;



        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.container, new MainFragment());

       // transaction.addToBackStack(null);

        transaction.commit();




    }




}


