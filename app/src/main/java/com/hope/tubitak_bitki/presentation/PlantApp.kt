package com.hope.tubitak_bitki.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hope.tubitak_bitki.presentation.navigation.BottomNavItem
import com.hope.tubitak_bitki.presentation.navigation.Screen

@Composable
fun PlantApp() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem("Ana Sayfa", Screen.Home.route, Icons.Default.Home),
        BottomNavItem("Tara", Screen.Camera.route, Icons.Default.CameraAlt),
        BottomNavItem("Geçmiş", Screen.History.route, Icons.Default.History),
        BottomNavItem("Profil", Screen.Profile.route, Icons.Default.Person)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isBottomBarVisible = when (currentDestination?.route) {
        Screen.Login.route, Screen.Register.route, Screen.Camera.route -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Screen.Login.route) {
                Text("Burası Login sayfası")
            }

            composable(Screen.Register.route) {
                Text("Burası Kayıt Sayfası")
            }

            composable(Screen.Home.route) {
                Text("Burası Ana Sayfa")
            }

            composable(Screen.Camera.route) {
                Text("Burası Camera")
            }

            composable(Screen.History.route) {
                Text("Burası Geçmiş")
            }

            composable(Screen.Profile.route) {
                Text("Burası Profil")
            }
        }
    }
}