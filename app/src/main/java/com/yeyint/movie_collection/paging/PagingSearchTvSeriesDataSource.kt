package com.yeyint.movie_collection.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yeyint.movie_collection.apiService.ApiService
import com.yeyint.movie_collection.helper.MovieConstant
import com.yeyint.movie_collection.model.MovieModel
import com.yeyint.movie_collection.model.TvSeriesModel
import com.yeyint.movie_collection.dao.TvSeriesDao

class PagingSearchTvSeriesDataSource(
    private val apiService: ApiService,
    private val searchKey: String
) : PagingSource<Int, MovieModel>() {

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        try {
            val currentLoadingPageKey = params.key ?: 1

            val response = apiService.searchTvSeries(searchKey, currentLoadingPageKey)
            val responseData = mutableListOf<MovieModel>()
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

            for (i in responseData) {
                i.type = MovieConstant.tvSeries
                val tvSeriesModel = TvSeriesModel(id = i.id, i.adult, i.backdropPath, i.originalLanguage, i.originalTitle, i.overview, i.popularity, i.posterPath, i.releaseDate,
                    i.title, i.video, i.voteAverage, i.voteCount, i.originalName, i. firstAirDate, i.type)
            }

            val nextKey = if (responseData.isEmpty()) null else currentLoadingPageKey + 1

            if(response.body()?.results?.isEmpty() == true){
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

            return LoadResult.Page(
                data = responseData,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}