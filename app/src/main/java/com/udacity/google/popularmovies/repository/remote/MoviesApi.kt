package com.udacity.google.popularmovies.repository.remote

import com.udacity.google.popularmovies.repository.data.MoviesResponse
import com.udacity.google.popularmovies.repository.data.ReviewsResponse
import com.udacity.google.popularmovies.repository.data.TrailersResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("discover/movie")
    fun getMovies(@Query("sort_by") sort_by: String, @Query("api_key") api_key: String): Observable<MoviesResponse>

    @GET("movie/{id}/videos")
    fun getTrailers(@Path("id") id: Int, @Query("api_key") api_key: String): Observable<TrailersResponse>

    @GET("movie/{id}/reviews")
    fun getReviews(@Path("id") id: Int, @Query("api_key") api_key: String): Observable<ReviewsResponse>
}
