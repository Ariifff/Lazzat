package com.arif.lazzat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arif.lazzat.api.RecipeDetailResponse
import com.arif.lazzat.api.RecipeRepository
import kotlinx.coroutines.launch


class RecipeDetailViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _recipeDetails = MutableLiveData<RecipeDetailResponse?>()
    val recipeDetails: LiveData<RecipeDetailResponse?> = _recipeDetails

    fun fetchRecipeDetails(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getRecipeDetails(id)
                _recipeDetails.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                _isLoading.value = false
            }
        }
    }
}
