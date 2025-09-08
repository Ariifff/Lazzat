package com.arif.lazzat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arif.lazzat.data.AppDatabase
import com.arif.lazzat.data.FavouriteEntity
import com.arif.lazzat.data.PantryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PantryFavouriteViewModel(private val db: AppDatabase) : ViewModel() {

    // Pantry items
    val pantryItems: Flow<List<PantryItem>> = db.pantryDao().getAllItems()

    fun addPantryItem(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.pantryDao().insertItem(PantryItem(name = name, isActive = false))
        }
    }

    fun deletePantryItem(item: PantryItem) {
        viewModelScope.launch (Dispatchers.IO){
            db.pantryDao().deleteItem(item)
        }
    }

    fun updatePantryItem(item: PantryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            db.pantryDao().updateItem(item)
        }
    }

    // =============================
    //   FAVOURITES SECTION
    // =============================
    val favouriteRecipes = db.favouriteDao().getAllFavourites().stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    fun addFavourite(recipe: FavouriteEntity) {
        viewModelScope.launch {
            db.favouriteDao().insertFavourite(recipe)
        }
    }

    fun removeFavourite(recipe: FavouriteEntity) {
        viewModelScope.launch (Dispatchers.IO){
            db.favouriteDao().deleteFavourite(recipe)
        }
    }

    fun toggleFavourite(recipe: FavouriteEntity) {
        viewModelScope.launch {
            val existing = db.favouriteDao().getRecipeById(recipe.id.toString())
            if (existing != null) {
                db.favouriteDao().deleteFavourite(existing)
            } else {
                db.favouriteDao().insertFavourite(recipe)
            }
        }
    }

    suspend fun isFavourite(id: Int): Boolean {
        return db.favouriteDao().getRecipeById(id.toString()) != null
    }
}
