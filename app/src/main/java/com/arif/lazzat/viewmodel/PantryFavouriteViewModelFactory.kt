package com.arif.lazzat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arif.lazzat.data.AppDatabase

class PantryFavouriteViewModelFactory(
    private val db: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PantryFavouriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PantryFavouriteViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
