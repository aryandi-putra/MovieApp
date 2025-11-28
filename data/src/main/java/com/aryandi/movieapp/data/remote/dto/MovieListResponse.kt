package com.aryandi.movieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Response wrapper for list of movies from API
 */
data class MovieListResponse(
    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val results: List<MovieDto>,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_results")
    val totalResults: Int
)
