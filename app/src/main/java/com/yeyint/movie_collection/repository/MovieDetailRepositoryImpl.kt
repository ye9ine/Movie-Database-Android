package com.yeyint.movie_collection.repository
import androidx.paging.PagingSource
import com.yeyint.movie_collection.apiService.ApiService
import com.yeyint.movie_collection.db.MovieDb
import com.yeyint.movie_collection.model.MovieModel
import com.yeyint.movie_collection.model.MoviePosterModel
import com.yeyint.movie_collection.model.MoviePosterResultModel
import com.yeyint.movie_collection.model.MovieResultModel
import com.yeyint.movie_collection.model.MovieTrailerResultModel
import com.yeyint.movie_collection.model.TvSeriesModel
import com.yeyint.movie_collection.model.UpcomingMovieModel
import com.yeyint.movie_collection.paging.PagingMovieDataSource
import com.yeyint.movie_collection.paging.PagingSearchMovieDataSource
import com.yeyint.movie_collection.paging.PagingSearchTvSeriesDataSource
import com.yeyint.movie_collection.paging.PagingTvSeriesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class MovieDetailRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    movieDb: MovieDb
) : MovieDetailRepository {

    private val moviePosterDao = movieDb.moviePosterDao()

    override fun getMoviePoster(movieId: Int): Flow<Response<MoviePosterResultModel>> {
        return flow {
            emit(apiService.getMoviePoster(movieId))
        }.flowOn(Dispatchers.IO)
    }

    override fun getTvPoster(tvId: Int): Flow<Response<MoviePosterResultModel>> {
        return flow {
            emit(apiService.getTvSeriesPoster(tvId))
        }.flowOn(Dispatchers.IO)
    }

    override fun getMovieTrailer(movieId: Int): Flow<Response<MovieTrailerResultModel>> {
        return flow {
            emit(apiService.getMovieTrailer(movieId))
        }.flowOn(Dispatchers.IO)
    }

    override fun getTvTrailer(tvId: Int): Flow<Response<MovieTrailerResultModel>> {
        return flow {
            emit(apiService.getTvSeriesTrailer(tvId))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insertPoster(moviePosterModel: MoviePosterModel) {
        moviePosterDao.insert(moviePosterModel = moviePosterModel)
    }

    override suspend fun getPosterFromDb(movieId: Int) : Flow<List<MoviePosterModel>> {
        return flow {
            emit(moviePosterDao.getPosterByMovie(movieId))
        }.flowOn(Dispatchers.IO)
    }


}