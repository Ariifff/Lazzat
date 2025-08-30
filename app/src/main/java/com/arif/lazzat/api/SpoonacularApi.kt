package com.arif.lazzat.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi {

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("includeIngredients") ingredients: String?,
        @Query("cuisine") cuisines: String?,  // comma-separated
        @Query("diet") diets: String?,        // comma-separated
        @Query("number") number: Int = 100,
        @Query("ranking") ranking: Int = 1,
        @Query("addRecipeInformation") addInfo: Boolean = true,
        @Query("apiKey") apiKey: String
    ): RecipeSearchResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String,
        @Query("includeNutrition") includeNutrition: Boolean = false
    ): RecipeDetailResponse

}
