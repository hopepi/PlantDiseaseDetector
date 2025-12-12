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
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hope.tubitak_bitki.presentation.camera.CameraScreen
import com.hope.tubitak_bitki.presentation.history.HistoryScreen
import com.hope.tubitak_bitki.presentation.home.HomeScreen
import com.hope.tubitak_bitki.presentation.login.LoginScreen
import com.hope.tubitak_bitki.presentation.navigation.BottomNavItem
import com.hope.tubitak_bitki.presentation.navigation.Screen
import com.hope.tubitak_bitki.presentation.profile.ProfileScreen
import com.hope.tubitak_bitki.presentation.register.RegisterScreen
import com.hope.tubitak_bitki.presentation.ui.theme.*

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
        containerColor = DarkForest,
        bottomBar = {
            if (isBottomBarVisible) {
                NavigationBar(
                    containerColor = Color.Black.copy(alpha = 0.8f),
                    contentColor = NeonGreen
                ) {
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
                            label = { Text(item.title) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonGreen,
                                selectedTextColor = NeonGreen,
                                indicatorColor = NeonGreen.copy(alpha = 0.2f),
                                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                                unselectedTextColor = Color.White.copy(alpha = 0.5f)
                            )
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
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    onCameraClick = {
                        navController.navigate(Screen.Camera.route)
                    }
                )
            }

            composable(Screen.Camera.route) {
                CameraScreen(
                    onCloseClick = {
                        navController.popBackStack()
                    },
                    onAnalyzeClick = { data ->

                        println("Analiz edilecek veri: $data")
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.History.route) {
                HistoryScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {

                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}