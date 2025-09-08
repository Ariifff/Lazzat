package com.arif.lazzat.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteEntity(
    @PrimaryKey val id: Int,   // Recipe ID from API
    val title: String,
    val image: String,

)

