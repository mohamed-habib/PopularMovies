package com.udacity.google.popularmovies.repository.remote

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MoviesRepository {
    private val BASE_URL = "http://api.themoviedb.org/3/"
    private val moviesAPI = create(BASE_URL)

    private fun create(baseUrl: String): MoviesApi {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create(MoviesApi::class.java)
    }

    fun getMovies(sort_by: String, api_key: String) = moviesAPI.getMovies(sort_by, api_key)

    fun getTrailers(id: Int, api_key: String) = moviesAPI.getTrailers(id, api_key)

    fun getReviews(id: Int, api_key: String) = moviesAPI.getReviews(id, api_key)

}