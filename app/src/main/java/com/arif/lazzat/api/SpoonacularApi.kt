package com.arif.lazzat.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi {

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("includeIngredients") includeIngredients: String?,
        @Query("cuisine") cuisines: String?,  // comma-separated
        @Query("diet") diets: String?,        // comma-separated
        @Query("number") number: Int = 100,
        @Query("ranking") ranking: Int = 1, // CHANGE 1: Default to ranking=2 for better sorting
        @Query("addRecipeInformation") addInfo: Boolean = true, // CHANGE 2: Set to FALSE to save credits
        @Query("apiKey") apiKey: String
    ): RecipeSearchResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String,
        @Query("includeNutrition") includeNutrition: Boolean = false
    ): RecipeDetailResponse

}

