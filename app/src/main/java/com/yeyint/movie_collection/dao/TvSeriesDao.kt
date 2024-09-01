package com.yeyint.movie_collection.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yeyint.movie_collection.model.TvSeriesModel

@Dao
interface TvSeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tvSeriesModel: TvSeriesModel)

    @Query("Select * From tvSeriesTable ORDER BY popularity DESC")
    suspend fun getAll() : List<TvSeriesModel>

    @Query("SELECT * FROM tvSeriesTable WHERE originalName LIKE '%' || :searchQuery || '%' ORDER BY popularity DESC")
    suspend fun searchTvSeries(searchQuery: String): List<TvSeriesModel>
}