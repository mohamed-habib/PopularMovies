package com.udacity.google.popularmovies.repository.data

import com.google.gson.annotations.SerializedName


data class MoviesResponse(
        @SerializedName("results")
        val movieList: List<Movie>
)