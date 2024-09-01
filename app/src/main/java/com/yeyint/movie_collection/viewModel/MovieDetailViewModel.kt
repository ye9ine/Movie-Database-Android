package com.yeyint.movie_collection.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeyint.movie_collection.helper.MovieConstant
import com.yeyint.movie_collection.model.MoviePosterModel
import com.yeyint.movie_collection.model.MovieVideosModel
import com.yeyint.movie_collection.repository.MovieDetailRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MoviePosterState {
    class Success(val data: List<MoviePosterModel>):MoviePosterState()
    class Exception(val t:Throwable):MoviePosterState()
    class OnFail(val message:String):MoviePosterState()
    data object Loading:MoviePosterState()
    data object Empty:MoviePosterState()
}

sealed class MovieTrailerState {
    class Success(val data: List<MovieVideosModel>):MovieTrailerState()
    class Exception(val t:Throwable):MovieTrailerState()
    class OnFail(val message:String):MovieTrailerState()
    data object Loading:MovieTrailerState()
    data object Empty:MovieTrailerState()
}

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieDetailRepositoryImpl: MovieDetailRepositoryImpl
) : ViewModel() {

    val movieTvPosterList = MutableLiveData<MoviePosterState>(MoviePosterState.Empty)
    val movieTvTrailerList = MutableLiveData<MovieTrailerState>(MovieTrailerState.Empty)
    val movieId = MutableLiveData<Int>()


    fun getMoviePoster(){
        viewModelScope.launch {
            movieDetailRepositoryImpl.getMoviePoster(movieId = movieId.value!!).onStart {
                movieTvPosterList.value = MoviePosterState.Loading
            }.catch {
                movieTvPosterList.value = MoviePosterState.Exception(it)
                getMoviePosterFromDb()
            }.collect{
                if (it.isSuccessful) {
                    val respBody = it.body()

                    respBody.let { value ->
                        val list = mutableListOf<MoviePosterModel>()
                        for(i in value?.posters!!){
                            list.add(i)
                            insertMoviePoster(i)
                        }
                        movieTvPosterList.value = MoviePosterState.Success(data = list)
                    }
                } else {
                    movieTvPosterList.value = MoviePosterState.OnFail("Something went wrong")
                }
            }
        }
    }

    private suspend fun insertMoviePoster(moviePosterModel: MoviePosterModel){
        movieDetailRepositoryImpl.insertPoster(moviePosterModel = moviePosterModel.copy(movieId = movieId.value!!))
    }

    private fun getMoviePosterFromDb(){
        viewModelScope.launch {
            movieDetailRepositoryImpl.getPosterFromDb(movieId = movieId.value!!).collect{
                movieTvPosterList.value = MoviePosterState.Success(data = it)
            }
        }
    }

    fun getTvPoster(){
        viewModelScope.launch {
            movieDetailRepositoryImpl.getTvPoster(tvId = movieId.value!!).onStart {
                movieTvPosterList.value = MoviePosterState.Loading
            }.catch {
                movieTvPosterList.value = MoviePosterState.Exception(it)
                getMoviePosterFromDb()
            }.collect{
                if (it.isSuccessful) {
                    val respBody = it.body()

                    respBody.let { value ->
                        val list = mutableListOf<MoviePosterModel>()
                        for(i in value?.posters!!){
                            list.add(i)
                            insertMoviePoster(moviePosterModel = i)
                        }
                        movieTvPosterList.value = MoviePosterState.Success(data = list)
                    }
                } else {
                    movieTvPosterList.value = MoviePosterState.OnFail("Something went wrong")
                }
            }
        }
    }

    fun getMovieTrailer(){
        viewModelScope.launch {
            movieDetailRepositoryImpl.getMovieTrailer(movieId = movieId.value!!).onStart {
                movieTvTrailerList.value = MovieTrailerState.Loading
            }.catch {
                movieTvTrailerList.value = MovieTrailerState.Exception(it)
            }.collect{
                if (it.isSuccessful) {
                    val respBody = it.body()

                    respBody.let { value ->
                        val list = mutableListOf<MovieVideosModel>()
                        for(i in value?.results!!){
                            list.add(i)
                        }
                        movieTvTrailerList.value = MovieTrailerState.Success(data = list)
                    }
                } else {
                    movieTvTrailerList.value = MovieTrailerState.OnFail("Something went wrong")
                }
            }
        }
    }

    fun getTvTrailer(){
        viewModelScope.launch {
            movieDetailRepositoryImpl.getTvTrailer(tvId = movieId.value!!).onStart {
                movieTvTrailerList.value = MovieTrailerState.Loading
            }.catch {
                movieTvTrailerList.value = MovieTrailerState.Exception(it)
            }.collect{
                if (it.isSuccessful) {
                    val respBody = it.body()

                    respBody.let { value ->
                        val list = mutableListOf<MovieVideosModel>()
                        for(i in value?.results!!){
                            list.add(i)
                        }
                        movieTvTrailerList.value = MovieTrailerState.Success(data = list)
                    }
                } else {
                    movieTvTrailerList.value = MovieTrailerState.OnFail("Something went wrong")
                }
            }
        }
    }
}