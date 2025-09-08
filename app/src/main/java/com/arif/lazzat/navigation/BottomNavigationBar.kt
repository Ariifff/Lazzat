package com.arif.lazzat.navigation

// navigation/BottomNavigationBar.kt
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomNavigationBar(navController: NavController) {
    Log.d("NAV_DEBUG", "BottomNavigationBar is composing")
    val items = listOf(
        BottomNavItem(
            route = Destinations.HOME,
            label = "Home",
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            route = Destinations.PANTRY,
            label ="Pantry",
            icon = Icons.Default.ShoppingCart
        ),
        BottomNavItem(
            route = Destinations.FAVOURITE,
            label = "Favourites",
            icon = Icons.Default.List
        ),
        BottomNavItem(
            route = Destinations.PROFILE,
            label = "Profile",
            icon = Icons.Default.Person
        )
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Log.d("NAV_DEBUG", "Current route in BottomBar: $currentRoute")

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)
