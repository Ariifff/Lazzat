package com.arif.lazzat.ui.screens.favourite

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.arif.lazzat.data.AppDatabase
import com.arif.lazzat.navigation.Destinations
import com.arif.lazzat.ui.theme.lightcolor
import com.arif.lazzat.ui.theme.secondarycolor
import com.arif.lazzat.viewmodel.PantryFavouriteViewModel
import com.arif.lazzat.viewmodel.PantryFavouriteViewModelFactory

@Composable
fun FavouriteScreenWrapper(
    navController: NavController
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val viewModel: PantryFavouriteViewModel = viewModel(
        factory = PantryFavouriteViewModelFactory(db)
    )
    FavouriteScreen(viewModel,navController )
}

@Composable
fun FavouriteScreen(viewModel: PantryFavouriteViewModel, navController: NavController) {
    val favourites by viewModel.favouriteRecipes.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var recipeToDelete by remember { mutableStateOf<com.arif.lazzat.data.FavouriteEntity?>(null) }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        // 🔥 Top section (just like pantry)
        FavouriteTopSection()
        Spacer(modifier = Modifier.height(16.dp))



        if (favourites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favourites yet",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray)
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(favourites) { fav ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clickable {
                                navController.navigate(
                                    "${Destinations.HOME_DETAIL}?" +
                                            "id=${fav.id}&" +
                                            "title=${Uri.encode(fav.title)}&" +
                                            "image=${Uri.encode(fav.image)}"
                                )
                            },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // ✅ Image on the left
                            AsyncImage(
                                model = fav.image,
                                contentDescription = fav.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )

                            // ✅ Title in the middle
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = fav.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 2
                                )
                                Text(
                                    text = "Saved Recipe",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )
                            }

                            // ✅ Delete button on the right
                            IconButton(
                                onClick = {
                                    recipeToDelete = fav
                                    showDialog = true },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error)
                            }

                        }
                    }
                }
            }
            // 🔥 Single dialog outside LazyColumn
            if (showDialog && recipeToDelete != null) {
                ConfirmDeleteDialog(
                    showDialog = showDialog,
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        viewModel.removeFavourite(recipeToDelete!!) // ✅ Offloaded to background inside VM
                        recipeToDelete = null
                        showDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun FavouriteTopSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        secondarycolor,
                        lightcolor
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✅ Use heart icon for favourites
            Icon(
                imageVector = Icons.Default.Favorite, // you can replace with Icons.Default.Favorite if you prefer
                contentDescription = "Favourites Icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "My Favourites",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your saved recipes in one place",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black
                    )
                )
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = "Delete Favourite")
            },
            text = {
                Text("Are you sure you want to remove this recipe from favourites?")
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}


