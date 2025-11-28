package com.aryandi.movieapp.di

import com.aryandi.movieapp.BuildConfig
import com.aryandi.movieapp.data.remote.api.MovieApiService
import com.aryandi.movieapp.data.repository.MovieRepositoryImpl
import com.aryandi.movieapp.domain.repository.MovieRepository
import com.aryandi.movieapp.domain.usecase.GetMovieDetailsUseCase
import com.aryandi.movieapp.domain.usecase.GetPopularMoviesUseCase
import com.aryandi.movieapp.domain.usecase.SearchMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Hilt module for dependency injection
 * This is where we wire up all our dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MovieApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApiService(retrofit: Retrofit): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiKey(): String {
        return BuildConfig.TMDB_API_KEY
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        apiService: MovieApiService,
        apiKey: String
    ): MovieRepository {
        return MovieRepositoryImpl(apiService, apiKey)
    }

    @Provides
    @Singleton
    fun provideGetPopularMoviesUseCase(
        repository: MovieRepository
    ): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMovieDetailsUseCase(
        repository: MovieRepository
    ): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchMoviesUseCase(
        repository: MovieRepository
    ): SearchMoviesUseCase {
        return SearchMoviesUseCase(repository)
    }
}
