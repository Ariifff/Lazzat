package com.arif.lazzat.api

data class RecipeDetailResponse(
    val id: Int,
    val title: String,
    val image: String,
    val summary: String?,
    val extendedIngredients: List<ExtendedIngredient>,
    val instructions: String
)

data class ExtendedIngredient(
    val id: Int,
    val original: String,  // e.g., "2 cups of rice"
    val name: String,      // e.g., "rice"
    val amount: Double,
    val unit: String
)
