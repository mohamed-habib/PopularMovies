package com.udacity.google.popularmovies.util

/**
 * Created by Dell on 10/4/2015.
 */
data class Movie(
        var id: Int = 0,
        var title: String,
        var overview: String,
        var release_date: String,
        var poster_path: String,
        var vote_average: String
)