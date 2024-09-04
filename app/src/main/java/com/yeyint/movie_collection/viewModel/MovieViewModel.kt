package com.yeyint.movie_collection.viewModel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.yeyint.movie_collection.helper.MovieConstant
import com.yeyint.movie_collection.model.MovieModel
import com.yeyint.movie_collection.model.UpcomingMovieModel
import com.yeyint.movie_collection.repository.MovieRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UpcomingMovieState {
    class Success(val data: List<MovieModel>):UpcomingMovieState()
    class Exception(val t:Throwable):UpcomingMovieState()
    class OnFail(val message:String):UpcomingMovieState()
    data object Loading:UpcomingMovieState()
    data object Empty:UpcomingMovieState()
}

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepositoryImpl: MovieRepositoryImpl,
) : ViewModel() {

    val upcomingMovieList = MutableLiveData<UpcomingMovieState>(UpcomingMovieState.Empty)
    val offlineMovieList = MutableLiveData<List<MovieModel>>()
    val searchKey = MutableLiveData("")

    init {
        getUpcomingMovie()
    }

    val movieList = Pager(PagingConfig(pageSize = 10)) {
        movieRepositoryImpl.getMovie()
    }.flow.cachedIn(viewModelScope)

    val tvSeriesList = Pager(PagingConfig(pageSize = 10)) {
        movieRepositoryImpl.getTv()
    }.flow.cachedIn(viewModelScope)

    var searchMovieList = Pager(PagingConfig(pageSize = 10)) {
        movieRepositoryImpl.searchMovie(searchKey.value!!)
    }.flow.cachedIn(viewModelScope)

    var searchTvList = Pager(PagingConfig(pageSize = 10)) {
        movieRepositoryImpl.searchTv(searchKey.value!!)
    }.flow.cachedIn(viewModelScope)

    fun searchMovie(){
        searchMovieList = Pager(PagingConfig(pageSize = 10)) {
            movieRepositoryImpl.searchMovie(searchKey.value!!)
        }.flow.cachedIn(viewModelScope)
    }

    fun searchTv(){
        searchTvList = Pager(PagingConfig(pageSize = 10)) {
            movieRepositoryImpl.searchTv(searchKey.value!!)
        }.flow.cachedIn(viewModelScope)
    }

    private fun getUpcomingMovie() {
        viewModelScope.launch {
            movieRepositoryImpl.getUpComingMovie().onStart {
                upcomingMovieList.value = UpcomingMovieState.Loading
            }.catch {
                upcomingMovieList.value = UpcomingMovieState.Exception(it)
                getUpcomingMovieFromDb()
            }.collect {
                if (it.isSuccessful) {
                    val respBody = it.body()

                    respBody.let { value ->
                        val list = mutableListOf<MovieModel>()
                        for(i in value!!.results){
                            list.add(i)
                            insertUpcomingMovie(i)
                        }
                        upcomingMovieList.value = UpcomingMovieState.Success(data = list)
                    }
                } else {
                    upcomingMovieList.value = UpcomingMovieState.OnFail("Something went wrong")
                }
            }
        }
    }

    private suspend fun insertUpcomingMovie(movieModel: MovieModel) {
        val model = UpcomingMovieModel(id = movieModel.id, movieModel.adult, movieModel.backdropPath, movieModel.originalLanguage, movieModel.originalTitle, movieModel.overview, movieModel.popularity, movieModel.posterPath, movieModel.releaseDate,
            movieModel.title, movieModel.video, movieModel.voteAverage, movieModel.voteCount, movieModel.originalName, movieModel.firstAirDate, movieModel.type)
        movieRepositoryImpl.insertUpcomingMovie(model)
    }

    private suspend fun getUpcomingMovieFromDb() {
        viewModelScope.launch {
            movieRepositoryImpl.getUpcomingMovieFromDb().collect {
                val list = mutableListOf<MovieModel>()
                for(i in it){
                    val movieModel = MovieModel(id = i.id, i.adult, i.backdropPath, i.originalLanguage, i.originalTitle, i.overview, i.popularity, i.posterPath, i.releaseDate,
                        i.title, i.video, i.voteAverage, i.voteCount, i.originalName, i. firstAirDate, i.type)
                    list.add(movieModel)
                }
                upcomingMovieList.value = UpcomingMovieState.Success(data = list)
            }
        }
    }

    fun getAllMovieAndTvFromDb(type: String, searchKey: String) {
        offlineMovieList.value = emptyList()
        viewModelScope.launch {
            if(type == MovieConstant.movie){
                if(searchKey.isNotEmpty()){
                    movieRepositoryImpl.searchMovieFromDb(searchKey).collect{
                        offlineMovieList.value = it
                    }
                }else{
                    movieRepositoryImpl.getMovieFromDb().collect{
                        offlineMovieList.value = it
                    }
                }
            }else{
                if(searchKey.isNotEmpty()){
                    movieRepositoryImpl.getTvFromDb().collect{
                        val list = mutableListOf<MovieModel>()
                        for (i in it) {
                            i.type = MovieConstant.tvSeries
                            val movieModel = MovieModel(id = i.id, i.adult, i.backdropPath, i.originalLanguage, i.originalTitle, i.overview, i.popularity, i.posterPath, i.releaseDate,
                                i.title, i.video, i.voteAverage, i.voteCount, i.originalName, i. firstAirDate, i.type)
                            list.add(movieModel)
                        }
                        offlineMovieList.value = list
                    }
                }else{
                    movieRepositoryImpl.searchTvFromDb(searchKey).collect{
                        val list = mutableListOf<MovieModel>()
                        for (i in it) {
                            i.type = MovieConstant.tvSeries
                            val movieModel = MovieModel(id = i.id, i.adult, i.backdropPath, i.originalLanguage, i.originalTitle, i.overview, i.popularity, i.posterPath, i.releaseDate,
                                i.title, i.video, i.voteAverage, i.voteCount, i.originalName, i. firstAirDate, i.type)
                            list.add(movieModel)
                        }
                        offlineMovieList.value = list
                    }
                }

            }
        }
    }
}