package com.udacity.google.popularmovies

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

import com.udacity.google.popularmovies.fragments.DetailFragment

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.detail_container, DetailFragment())
        transaction.commit()
    }
}
