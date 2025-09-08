
package com.arif.lazzat.api

class RecipeRepository(private val api: SpoonacularApi) {

    suspend fun searchRecipes(
        ingredients: String?,
        cuisines: List<String>?,
        diets: List<String>?
    ): RecipeSearchResponse {
        return api.searchRecipes(
            apiKey = "1743fe933df34554a8babfa88594d6c2",
            includeIngredients = ingredients,
            cuisines = cuisines?.joinToString(","),
            diets = diets?.joinToString(","),
            number = 100,
            // ranking = 1 // CHANGE: Remove this line. Now it will use the default (2) from the interface.
            // addInfo = true // This is also now false by default in the interface.
        )
    }

    suspend fun getRecipeDetails(recipeId: Int): RecipeDetailResponse {
        return api.getRecipeDetails(
            recipeId = recipeId,
            apiKey = "1743fe933df34554a8babfa88594d6c2"
        )
    }
}

