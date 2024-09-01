package com.yeyint.movie_collection.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yeyint.movie_collection.model.MovieModel

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieModel: MovieModel)

    @Query("Select * From movieTable ORDER BY popularity DESC")
    suspend fun getAll() : List<MovieModel>

    @Query("SELECT * FROM movieTable WHERE originalTitle LIKE '%' || :searchQuery || '%' ORDER BY popularity DESC")
    suspend fun searchMovie(searchQuery: String): List<MovieModel>
}