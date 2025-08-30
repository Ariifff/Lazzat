package com.arif.lazzat.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_recipes")
data class FavouriteRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String
)
