package com.udacity.google.popularmovies.repository.data

import com.google.gson.annotations.SerializedName


data class ReviewsResponse(
        @SerializedName("results")
        val reviewsList: List<Review>
)