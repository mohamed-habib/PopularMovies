package com.udacity.google.popularmovies.repository

import com.google.gson.annotations.SerializedName
import com.udacity.google.popularmovies.util.Movie


data class MoviesResponse(
        @SerializedName("results")
        val movieList: List<Movie>
)