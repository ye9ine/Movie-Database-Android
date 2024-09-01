package com.yeyint.movie_collection.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yeyint.movie_collection.model.MovieModel
import com.yeyint.movie_collection.model.MoviePosterModel

@Dao
interface MoviePosterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moviePosterModel: MoviePosterModel)

    @Query("Select * From movie_poster")
    suspend fun getAll() : List<MoviePosterModel>

    @Query("SELECT * FROM movie_poster WHERE movieId = :movieId")
    suspend fun getPosterByMovie(movieId: Int): List<MoviePosterModel>
}