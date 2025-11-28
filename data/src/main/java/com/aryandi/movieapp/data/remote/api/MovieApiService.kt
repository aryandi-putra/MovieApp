package com.aryandi.movieapp.data.remote.api

import com.aryandi.movieapp.data.remote.dto.MovieDto
import com.aryandi.movieapp.data.remote.dto.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service interface
 * Defines endpoints for The Movie Database API
 */
interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MovieListResponse

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }
}
