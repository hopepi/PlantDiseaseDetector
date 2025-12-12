package com.hope.tubitak_bitki.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Home : Screen("home_screen")
    object Camera : Screen("camera_screen")
    object History : Screen("history_screen")
    object Profile : Screen("profile_screen")

    object Detail : Screen("detail_screen/{plantId}") {
        fun createRoute(plantId: Int) = "detail_screen/$plantId"
    }
}