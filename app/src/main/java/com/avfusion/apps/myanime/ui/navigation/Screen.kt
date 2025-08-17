package com.avfusion.apps.myanime.ui.navigation

sealed class Screen(val route: String) {
    object AnimeList : Screen("anime_list")
    object AnimeDetails : Screen("anime_details/{animeId}") {
        fun createRoute(animeId: Int) = "anime_details/$animeId"
    }
}