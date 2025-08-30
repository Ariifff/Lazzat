package com.arif.lazzat.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: FavouriteRecipe)

    @Delete
    suspend fun deleteRecipe(recipe: FavouriteRecipe)

    @Query("SELECT * FROM favourite_recipes")
    fun getAllRecipes(): Flow<List<FavouriteRecipe>>
}
