package com.aryandi.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aryandi.movieapp.presentation.movies.MoviesScreen
import com.aryandi.movieapp.ui.theme.MovieAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity - Entry point
 * @AndroidEntryPoint enables Hilt injection
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieAppTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "movies",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("movies") {
                            MoviesScreen(
                                onMovieClick = { movieId ->
                                    navController.navigate("movie/$movieId")
                                }
                            )
                        }

                        // Add more destinations as needed
                        // composable("movie/{movieId}") { ... }
                    }
                }
            }
        }
    }
}
