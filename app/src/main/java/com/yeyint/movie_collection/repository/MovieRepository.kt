package com.yeyint.movie_collection.repository

import androidx.paging.PagingSource
import com.yeyint.movie_collection.model.MovieModel
import com.yeyint.movie_collection.model.MovieResultModel
import com.yeyint.movie_collection.model.TvSeriesModel
import com.yeyint.movie_collection.model.UpcomingMovieModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieRepository {
    fun getMovie(): PagingSource<Int, MovieModel>
    fun getTv(): PagingSource<Int, MovieModel>
    fun getUpComingMovie(): Flow<Response<MovieResultModel>>
    fun getMovieFromDb(): Flow<List<MovieModel>>
    fun getTvFromDb(): Flow<List<TvSeriesModel>>
    fun getUpcomingMovieFromDb(): Flow<List<UpcomingMovieModel>>
    suspend fun insertUpcomingMovie(upcomingMovieModel: UpcomingMovieModel)
    fun searchMovie(searchKey: String): PagingSource<Int, MovieModel>
    fun searchTv(searchKey: String): PagingSource<Int, MovieModel>
    fun searchMovieFromDb(searchKey: String): Flow<List<MovieModel>>
    fun searchTvFromDb(searchKey: String): Flow<List<TvSeriesModel>>
}