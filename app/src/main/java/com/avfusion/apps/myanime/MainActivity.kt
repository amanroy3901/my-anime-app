package com.avfusion.apps.myanime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.avfusion.apps.myanime.ui.navigation.Screen
import com.avfusion.apps.myanime.ui.screens.topanime.AnimeListScreen
import com.avfusion.apps.myanime.ui.screens.animedetails.AnimeDetailsScreen
import com.avfusion.apps.myanime.ui.theme.MyAnimeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAnimeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.AnimeList.route
                ) {
                    composable(route = Screen.AnimeList.route) {
                        AnimeListScreen(navController = navController)
                    }
                    composable(
                        route = Screen.AnimeDetails.route,
                        arguments = listOf(navArgument("animeId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val animeId = backStackEntry.arguments?.getInt("animeId")
                        if (animeId != null) {
                            AnimeDetailsScreen()
                        }
                    }
                }
            }
        }
    }
}