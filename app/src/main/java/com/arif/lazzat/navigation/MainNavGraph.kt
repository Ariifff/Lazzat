package com.arif.lazzat.navigation

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arif.lazzat.ui.screens.favourite.FavouriteScreen
import com.arif.lazzat.ui.screens.favourite.FavouriteScreenWrapper
import com.arif.lazzat.ui.screens.home.HomeScreen
import com.arif.lazzat.ui.screens.profile.ProfileScreen
import com.arif.lazzat.ui.screens.home.RecipeDetailScreen
import com.arif.lazzat.ui.screens.home.SearchResultScreen
import com.arif.lazzat.ui.screens.pantry.PantryScreenWrapper

@Composable
fun MainNavGraph() {
    val navController = rememberNavController()

    // Initialize navigation AFTER graph is set
    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("NAV_FIX", "Navigated to: ${destination.route}")
        }
    }


    // Show bottom bar only on main screens
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in setOf(
        Destinations.HOME,
        Destinations.PANTRY,
        Destinations.FAVOURITE,
        Destinations.PROFILE
    )


    Scaffold(
        bottomBar = {
            Log.d("NAV_DEBUG", "Bottom bar should show: $showBottomBar")
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { padding ->
        Log.d("NAV_DEBUG", "Scaffold padding - Bottom: ${padding.calculateBottomPadding()}")

        NavHost(
            navController = navController,
            startDestination = Destinations.HOME
        ) {
            // ======================
            // HOME TAB FLOW
            // ======================
            composable(Destinations.HOME) {
                Log.d("SCREEN_DEBUG", "HomeScreen rendering")
                HomeScreen(
                    navController= navController
                )

            }
            composable(
                route = Destinations.HOME_SEARCH + "?ingredients={ingredients}&cuisines={cuisines}&diets={diets}",
                arguments = listOf(
                    navArgument("ingredients") { type = NavType.StringType; defaultValue = "" },
                    navArgument("cuisines") { type = NavType.StringType; defaultValue = "" },
                    navArgument("diets") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backStackEntry ->

                SearchResultScreen(
                    navController = navController,
                )
            }

            composable(
                route = Destinations.HOME_DETAIL +
                        "?id={id}&title={title}&image={image}",
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType; defaultValue = "" },
                    navArgument("title") { type = NavType.StringType; defaultValue = "" },
                    navArgument("image") { type = NavType.StringType; defaultValue = "" },
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                val title = backStackEntry.arguments?.getString("title") ?: ""
                val image = backStackEntry.arguments?.getString("image") ?: ""

                RecipeDetailScreen(
                    id = id,
                    title = title,
                    image = image
                )
            }



            // ======================
            // PANTRY TAB FLOW
            // ======================
            composable(Destinations.PANTRY) {
                Log.d("SCREEN_DEBUG", "PantryScreen rendering")
                PantryScreenWrapper()
            }


            // ======================
            // FAVOURITE TAB FLOW
            // ======================

            composable(Destinations.FAVOURITE) {
                Log.d("SCREEN_DEBUG", "FavouriteScreen rendering")
                FavouriteScreenWrapper(navController)
            }


            // ======================
            // PROFILE TAB FLOW
            // ======================
            composable(Destinations.PROFILE) {
                Log.d("SCREEN_DEBUG", "ProfileScreen rendering")
                ProfileScreen(padding = padding)
            }

        }
    }
}