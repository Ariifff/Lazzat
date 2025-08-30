package com.arif.lazzat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arif.lazzat.data.AppDatabase
import com.arif.lazzat.data.FavouriteRecipe
import com.arif.lazzat.data.PantryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PantryFavouriteViewModel(private val db: AppDatabase) : ViewModel() {

    val pantryItems: Flow<List<PantryItem>> = db.pantryDao().getAllItems()
    val favouriteRecipes: Flow<List<FavouriteRecipe>> = db.favouriteDao().getAllRecipes()

    fun addPantryItem(name: String) {
        viewModelScope.launch {
            Log.d("PANTRY_DEBUG", "Adding item: $name")
            db.pantryDao().insertItem(PantryItem(name = name, isActive = false))
        }
    }

    fun addFavouriteRecipe(title: String, description: String) {
        viewModelScope.launch {
            db.favouriteDao().insertRecipe(FavouriteRecipe(title = title, description = description))
        }
    }

    fun deletePantryItem(item: PantryItem) {
        viewModelScope.launch {
            db.pantryDao().deleteItem(item)
        }
    }

    fun deleteFavouriteRecipe(recipe: FavouriteRecipe) {
        viewModelScope.launch {
            db.favouriteDao().deleteRecipe(recipe)
        }
    }

    fun updatePantryItem(item: PantryItem) {
        viewModelScope.launch {
            db.pantryDao().updateItem(item)
        }
    }

}
