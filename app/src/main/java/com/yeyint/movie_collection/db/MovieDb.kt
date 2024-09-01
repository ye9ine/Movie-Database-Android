package com.yeyint.movie_collection.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yeyint.movie_collection.model.MovieModel
import com.yeyint.movie_collection.model.TvSeriesModel
import com.yeyint.movie_collection.dao.MovieDao
import com.yeyint.movie_collection.dao.MoviePosterDao
import com.yeyint.movie_collection.dao.TvSeriesDao
import com.yeyint.movie_collection.dao.UpcomingMovieDao
import com.yeyint.movie_collection.model.MoviePosterModel
import com.yeyint.movie_collection.model.UpcomingMovieModel

@Database(
    entities = [MovieModel::class, TvSeriesModel::class, UpcomingMovieModel::class, MoviePosterModel::class],
    version = 1,
)
abstract class MovieDb: RoomDatabase() {
    abstract fun movieDao() : MovieDao
    abstract fun tvSeriesDao() : TvSeriesDao
    abstract fun upcomingMovieDao() : UpcomingMovieDao
    abstract fun moviePosterDao() : MoviePosterDao
}