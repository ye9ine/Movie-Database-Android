package com.yeyint.movie_collection.repository
import androidx.paging.PagingSource
import com.yeyint.movie_collection.apiService.ApiService
import com.yeyint.movie_collection.db.MovieDb
import com.yeyint.movie_collection.model.MovieModel
import com.yeyint.movie_collection.model.MovieResultModel
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

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    movieDb: MovieDb
) : MovieRepository {

    private val movieDao = movieDb.movieDao()
    private val tvSeriesDao = movieDb.tvSeriesDao()
    private val upcomingMovieDao = movieDb.upcomingMovieDao()

    override fun getMovie(): PagingSource<Int, MovieModel> {
        return PagingMovieDataSource(apiService, movieDao)
    }

    override fun getTv(): PagingSource<Int, MovieModel> {
        return PagingTvSeriesDataSource(apiService, tvSeriesDao)
    }

    override fun getUpComingMovie(): Flow<Response<MovieResultModel>> {
        return flow {
            emit(apiService.getUpcomingMovie())
        }.flowOn(Dispatchers.IO)
    }

    override fun getMovieFromDb(): Flow<List<MovieModel>> {
        return flow {
            emit(movieDao.getAll())
        }.flowOn(Dispatchers.IO)
    }

    override fun getTvFromDb(): Flow<List<TvSeriesModel>> {
        return flow {
            emit(tvSeriesDao.getAll())
        }.flowOn(Dispatchers.IO)
    }

    override fun getUpcomingMovieFromDb(): Flow<List<UpcomingMovieModel>> {
        return flow {
            emit(upcomingMovieDao.getAll())
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insertUpcomingMovie(upcomingMovieModel: UpcomingMovieModel) {
        upcomingMovieDao.insert(upcomingMovieModel)
    }

    override fun searchMovie(searchKey: String): PagingSource<Int, MovieModel> {
        return PagingSearchMovieDataSource(apiService, searchKey)
    }

    override fun searchTv(searchKey: String): PagingSource<Int, MovieModel> {
        return PagingSearchTvSeriesDataSource(apiService, searchKey)
    }

    override fun searchMovieFromDb(searchKey: String): Flow<List<MovieModel>> {
        return flow {
            emit(movieDao.searchMovie(searchKey))
        }.flowOn(Dispatchers.IO)
    }

    override fun searchTvFromDb(searchKey: String): Flow<List<TvSeriesModel>> {
        return flow {
            emit(tvSeriesDao.searchTvSeries(searchKey))
        }.flowOn(Dispatchers.IO)
    }


}