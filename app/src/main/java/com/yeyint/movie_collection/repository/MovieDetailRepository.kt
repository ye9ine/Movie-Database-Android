package com.yeyint.movie_collection.repository

import androidx.paging.PagingSource
import com.yeyint.movie_collection.model.MovieModel
import com.yeyint.movie_collection.model.MoviePosterModel
import com.yeyint.movie_collection.model.MoviePosterResultModel
import com.yeyint.movie_collection.model.MovieResultModel
import com.yeyint.movie_collection.model.MovieTrailerResultModel
import com.yeyint.movie_collection.model.TvSeriesModel
import com.yeyint.movie_collection.model.UpcomingMovieModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieDetailRepository {
    fun getMoviePoster(movieId: Int): Flow<Response<MoviePosterResultModel>>
    fun getTvPoster(tvId: Int): Flow<Response<MoviePosterResultModel>>
    fun getMovieTrailer(movieId: Int): Flow<Response<MovieTrailerResultModel>>
    fun getTvTrailer(tvId: Int): Flow<Response<MovieTrailerResultModel>>
    suspend fun insertPoster(moviePosterModel: MoviePosterModel)
    suspend fun getPosterFromDb(movieId: Int): Flow<List<MoviePosterModel>>
}