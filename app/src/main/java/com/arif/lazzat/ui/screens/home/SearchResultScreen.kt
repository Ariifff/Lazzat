package com.arif.lazzat.ui.screens.home

import RecipeViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.arif.lazzat.api.RecipeItem
import androidx.compose.runtime.livedata.observeAsState
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.saveable.rememberSaveable
import com.arif.lazzat.api.RecipeRepository
import com.arif.lazzat.api.RetrofitInstance
import com.arif.lazzat.navigation.Destinations
import com.arif.lazzat.viewmodel.RecipeViewModelFactory


@SuppressLint("UnrememberedGetBackStackEntry")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    navController: NavController,

) {
    val arguments = navController.currentBackStackEntry?.arguments
    val ingredientsArg = arguments?.getString("ingredients") ?: ""
    val cuisinesArg = arguments?.getString("cuisines") ?: ""
    val dietsArg = arguments?.getString("diets") ?: ""

    val repository = remember { RecipeRepository(RetrofitInstance.api) }


    val recipeViewModel: RecipeViewModel = viewModel(
        viewModelStoreOwner = navController.currentBackStackEntry!!,
        factory = RecipeViewModelFactory(repository)
    )

    val isLoading by recipeViewModel.isLoading.observeAsState(false)
    val recipes by recipeViewModel.recipes.observeAsState(emptyList())


    var hasInitialSearch by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(ingredientsArg, cuisinesArg, dietsArg) {
        // ADD THIS CONDITION: Only search if we haven't searched before AND have ingredients
        if (!hasInitialSearch && ingredientsArg.isNotEmpty()) {
            Log.d("API_TEST", "Triggering INITIAL search from ResultScreen")
            recipeViewModel.searchRecipes(
                ingredients = ingredientsArg,
                cuisines = cuisinesArg.split(",").filter { it.isNotEmpty() },
                diets = dietsArg.split(",").filter { it.isNotEmpty() }
            )
            hasInitialSearch = true // MARK AS SEARCHED
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Results", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            recipes.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No results found", style = MaterialTheme.typography.titleMedium)
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            "Recipes",
                            fontSize = 18.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    items(
                        items = recipes,
                        key = { item -> item.id }
                    ) { recipe ->
                        DishCardApi(recipe = recipe, navController = navController)
                    }
                }
            }
        }
    }
}


@Composable
fun DishCardApi(recipe: RecipeItem, navController: NavController, modifier: Modifier = Modifier) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .height(240.dp) // Slightly increased height to fit new info
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                // CHANGED: Removed summary from navigation as it will be null
                navController.navigate(
                    "${Destinations.HOME_DETAIL}?" +
                            "id=${recipe.id}&" +
                            "title=${Uri.encode(recipe.title)}&" +
                            "image=${Uri.encode(recipe.image)}"
                )
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )

            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = recipe.title,
                    fontSize = 16.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2 // Ensure long titles don't overflow
                )

            }
        }
    }
}
