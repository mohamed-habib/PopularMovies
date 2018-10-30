package com.udacity.google.popularmovies.repository.data

import com.google.gson.annotations.SerializedName


data class TrailersResponse(
        @SerializedName("results")
        val trailserList: List<Trailer>
)