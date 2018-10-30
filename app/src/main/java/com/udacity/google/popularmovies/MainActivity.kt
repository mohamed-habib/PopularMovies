package com.udacity.google.popularmovies

import android.app.ActionBar
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.udacity.google.popularmovies.fragments.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, MainFragment())
        transaction.commit()
    }
}


