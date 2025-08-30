package com.arif.lazzat.ui.screens.home

import RecipeViewModel
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arif.lazzat.R
import com.arif.lazzat.data.AppDatabase
import com.arif.lazzat.viewmodel.PantryFavouriteViewModel
import com.arif.lazzat.viewmodel.PantryFavouriteViewModelFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.arif.lazzat.api.RecipeRepository
import com.arif.lazzat.api.RetrofitInstance
import com.arif.lazzat.navigation.Destinations
import com.arif.lazzat.ui.theme.darkcolor
import com.arif.lazzat.viewmodel.RecipeViewModelFactory

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    navController : NavController
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val viewModel: PantryFavouriteViewModel = viewModel(
        factory = PantryFavouriteViewModelFactory(db)
    )

    val repository = RecipeRepository(RetrofitInstance.api)

    val recipeViewModel: RecipeViewModel = viewModel(

        factory = RecipeViewModelFactory(repository)
    )

    val pantryItems by viewModel.pantryItems.collectAsStateWithLifecycle(initialValue = emptyList())

    var ingredientInput by remember { mutableStateOf("") }
    var selectedCuisines by remember { mutableStateOf(setOf<String>()) }
    var selectedDiets by remember { mutableStateOf(setOf<String>()) }

    val cuisines = listOf("Indian", "Italian", "Chinese", "Mexican", "Japanese")
    val diets = listOf("Vegetarian", "Vegan", "Keto", "Paleo", "Gluten-Free")

    fun addIngredient(item: String) {
        val tokens =
            ingredientInput
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .toMutableList()
        if (!tokens.contains(item)) {
            tokens.add(item)
            ingredientInput = tokens.joinToString(", ")
        }
    }


    val listState = rememberLazyListState()

// fade progress based on scroll offset
    val bannerHeightPx = with(LocalDensity.current) { 200.dp.toPx() }
    val fadeProgress = (1f - (listState.firstVisibleItemScrollOffset / bannerHeightPx))
        .coerceIn(0f, 1f)

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF7)),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 80.dp, start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        // Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .alpha(fadeProgress)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, topEnd = 24.dp))


            ) {
                Image(
                    painter = painterResource(id = R.drawable.lazzat_logo),
                    contentDescription = "Food Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                )

                Text(
                    text = "Welcome to Lazzat",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Title
        item {
            Text(
                "Select Ingredients",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF5D4037)
            )
        }

        // Pantry Chips
        if (pantryItems.any { it.isActive }) {
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pantryItems.filter { it.isActive }.forEach { item ->
                        SuggestionChip(
                            onClick = { addIngredient(item.name) },
                            label = { Text(item.name) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = Color(0xFFFFE0B2),
                                labelColor = Color(0xFF5D4037)
                            )
                        )
                    }
                }
            }
        }

        // Ingredient Input
        item {
            OutlinedTextField(
                value = ingredientInput,
                onValueChange = { ingredientInput = it },
                label = { Text("Enter ingredients") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Cuisine chips
        item {
            Column {
                Text("Cuisine", fontWeight = FontWeight.SemiBold, color = Color(0xFF5D4037))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    cuisines.forEach { cuisine ->
                        val isSelected = cuisine in selectedCuisines
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedCuisines = if (isSelected) {
                                    selectedCuisines - cuisine
                                } else {
                                    selectedCuisines + cuisine
                                }
                            },
                            label = { Text(cuisine) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFFF7043),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Diet chips
        item {
            Column {
                Text("Diet", fontWeight = FontWeight.SemiBold, color = Color(0xFF5D4037))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    diets.forEach { diet ->
                        val isSelected = diet in selectedDiets
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedDiets = if (isSelected) {
                                    selectedDiets - diet
                                } else {
                                    selectedDiets + diet
                                }
                            },
                            label = { Text(diet) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF66BB6A),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Search Button
        item {
            Button(
                onClick = {
                    if (ingredientInput.isEmpty()) {
                        Toast.makeText(context, "Please enter at least one ingredient", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        // trigger API call
                        recipeViewModel.searchRecipes(
                            ingredients = ingredientInput,
                            cuisines = selectedCuisines.toList(),
                            diets = selectedDiets.toList()
                        )
                        // navigate only, results will be observed in results screen
                        navController.navigate(Destinations.HOME_SEARCH)
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = darkcolor)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Search Recipes", color = Color.White)
            }
        }
    }
}