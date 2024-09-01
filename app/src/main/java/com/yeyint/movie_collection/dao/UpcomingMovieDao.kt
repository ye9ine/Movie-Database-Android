package com.yeyint.movie_collection.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yeyint.movie_collection.model.UpcomingMovieModel

@Dao
interface UpcomingMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(upcomingMovieModel: UpcomingMovieModel)

    @Query("Select * From upcomingMovieTable ORDER BY popularity DESC")
    suspend fun getAll() : List<UpcomingMovieModel>

    @Query("SELECT * FROM upcomingMovieTable WHERE title LIKE '%' || :searchQuery || '%'")
    suspend fun searchTvSeries(searchQuery: String): List<UpcomingMovieModel>
}