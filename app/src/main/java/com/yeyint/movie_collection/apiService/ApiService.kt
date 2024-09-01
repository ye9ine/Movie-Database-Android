package com.yeyint.movie_collection.apiService

import com.yeyint.movie_collection.model.MoviePosterResultModel
import com.yeyint.movie_collection.model.MovieResultModel
import com.yeyint.movie_collection.model.MovieTrailerResultModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("discover/movie")
    suspend fun getMovie(@Query(value = "page") pageKey:Int) : Response<MovieResultModel>

    @GET("discover/tv")
    suspend fun getTv(@Query(value = "page") pageKey:Int) : Response<MovieResultModel>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovie() : Response<MovieResultModel>

    @GET("search/movie")
    suspend fun searchMovie(@Query(value = "query") searchKey:String, @Query(value = "page") date:Int) : Response<MovieResultModel>

    @GET("search/tv")
    suspend fun searchTvSeries(@Query(value = "query") searchKey:String, @Query(value = "page") date:Int) : Response<MovieResultModel>

    @GET("movie/{movieId}/images")
    suspend fun getMoviePoster(@Path(value = "movieId") movieId:Int) : Response<MoviePosterResultModel>

    @GET("tv/{tvId}/images")
    suspend fun getTvSeriesPoster(@Path(value = "tvId") movieId:Int) : Response<MoviePosterResultModel>

    @GET("movie/{movieId}/videos")
    suspend fun getMovieTrailer(@Path(value = "movieId") movieId:Int) : Response<MovieTrailerResultModel>

    @GET("tv/{tvId}/videos")
    suspend fun getTvSeriesTrailer(@Path(value = "tvId") movieId:Int) : Response<MovieTrailerResultModel>

}