package com.arif.lazzat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arif.lazzat.api.RecipeDetailResponse
import com.arif.lazzat.api.RecipeRepository
import kotlinx.coroutines.launch

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val recipe: RecipeDetailResponse) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}

class RecipeDetailViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipeDetails = MutableLiveData<RecipeDetailResponse?>()
    val recipeDetails: LiveData<RecipeDetailResponse?> = _recipeDetails

    fun fetchRecipeDetails(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getRecipeDetails(id)
                _recipeDetails.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
