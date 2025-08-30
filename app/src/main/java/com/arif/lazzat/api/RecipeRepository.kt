
package com.arif.lazzat.api

class RecipeRepository(private val api: SpoonacularApi) {

    suspend fun searchRecipes(
        ingredients: String?,
        cuisines: List<String>?,
        diets: List<String>?
    ): RecipeSearchResponse {
        return api.searchRecipes(
            apiKey = "1743fe933df34554a8babfa88594d6c2",
            ingredients = ingredients,
            cuisines = cuisines?.joinToString(","),
            diets = diets?.joinToString(","),
            number = 100,
            ranking = 1
        )
    }
    suspend fun getRecipeDetails(recipeId: Int): RecipeDetailResponse {
        return api.getRecipeDetails(
            recipeId = recipeId,
            apiKey = "1743fe933df34554a8babfa88594d6c2"
        )
    }
}

