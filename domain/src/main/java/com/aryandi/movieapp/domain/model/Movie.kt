package com.aryandi.movieapp.domain.model

/**
 * Domain entity representing a Movie
 * This is a pure Kotlin class with no Android dependencies
 */
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int
)
