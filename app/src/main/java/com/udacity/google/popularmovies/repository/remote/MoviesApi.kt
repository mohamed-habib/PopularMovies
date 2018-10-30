package com.udacity.google.popularmovies.repository.remote

import com.udacity.google.popularmovies.repository.MoviesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("movie")
    fun getMovies(@Query("sort_by") sort_by: String, @Query("api_key") api_key: String): Observable<MoviesResponse>

}