package com.arif.lazzat.api

data class RecipeSearchResponse(
    val results: List<RecipeItem> // This stays the same
)


data class RecipeItem(
    val id: Int,
    val title: String,
    val image: String,
    val summary: String,
    val instructions: String,

    )