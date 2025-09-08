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
    suspend fun insertFavourite(recipe: FavouriteEntity)

    @Delete
    suspend fun deleteFavourite(recipe: FavouriteEntity)

    @Query("SELECT * FROM favourites")
    fun getAllFavourites(): Flow<List<FavouriteEntity>>

    @Query("SELECT * FROM favourites WHERE id = :id LIMIT 1")
    suspend fun getRecipeById(id: String): FavouriteEntity?
}
