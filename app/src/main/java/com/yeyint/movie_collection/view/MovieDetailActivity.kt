package com.yeyint.movie_collection.view

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Pair
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController
import com.yeyint.movie_collection.adapter.MoviePosterAdapter
import com.yeyint.movie_collection.databinding.ActivityMovieDetailBinding
import com.yeyint.movie_collection.glideModule.GlideApp
import com.yeyint.movie_collection.helper.MovieConstant
import com.yeyint.movie_collection.model.MovieModel
import com.yeyint.movie_collection.viewModel.MovieDetailViewModel
import com.yeyint.movie_collection.viewModel.MoviePosterState
import com.yeyint.movie_collection.viewModel.MovieTrailerState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private lateinit var movieDetailBinding: ActivityMovieDetailBinding
    private val movieDetailViewModel: MovieDetailViewModel by viewModels()
    private lateinit var moviePosterAdapter: MoviePosterAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var youtubeVideoId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init view binding
        movieDetailBinding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(movieDetailBinding.root)
        init()
    }

    private fun init(){

        //get data from pass intent
        val movieModel = getParcelableExtra(intent, "obj", MovieModel::class.java)

        //init adapter and layout manager
        linearLayoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL, false)
        moviePosterAdapter = MoviePosterAdapter(this)

        movieDetailViewModel.movieId.value = movieModel?.id

        // fetch poster and trailer api
        if(movieModel?.type == MovieConstant.movie){
            movieDetailViewModel.getMoviePoster()
            movieDetailViewModel.getMovieTrailer()
        }else{
            movieDetailViewModel.getTvPoster()
            movieDetailViewModel.getTvTrailer()
        }

        movieDetailBinding.apply {

            //binding data to view
            val releaseYear = if (movieModel?.type == MovieConstant.movie) movieModel.releaseDate?.split('-')?.get(0) else movieModel?.firstAirDate?.split('-')?.get(0)
            if (movieModel != null) {
                tvTitle.text = if (movieModel.type == MovieConstant.movie) "${movieModel.title} (${releaseYear})" else "${movieModel.originalName} (${releaseYear})"
            }
            rtBar.rating = movieModel?.voteAverage?.div(2).toString().toFloat()
            tvRating.text = String.format("%.1f", movieModel?.voteAverage)
            tvOverview.text = movieModel?.overview

            recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = moviePosterAdapter
            }

            if(movieModel?.posterPath != null){
                GlideApp.with(applicationContext)
                    .load(MovieConstant.basePosterPath + movieModel.posterPath)
                    .into(imgPoster)
            }

            if(movieModel?.backdropPath != null){
                GlideApp.with(applicationContext)
                    .load(MovieConstant.baseBackdropPath + movieModel.backdropPath)
                    .into(imgBackDrop)
            }

            imgPoster.setOnClickListener { onClickPhotoDetail(this@MovieDetailActivity,imgPoster, movieModel!!.posterPath!!) }

            imgBackDrop.setOnClickListener { onClickPhotoDetail(this@MovieDetailActivity, imgBackDrop, movieModel!!.backdropPath!!) }

            imgBackButton.setOnClickListener{ finish() }

            lifecycle.addObserver(youtubePlayerView)

        }

        movieDetailBinding.youtubePlayerView.addFullScreenListener(object :YouTubePlayerFullScreenListener{
            override fun onYouTubePlayerEnterFullScreen() {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=${youtubeVideoId}"))
                intent.putExtra("force_fullscreen", true)
                startActivity(intent)
            }

            override fun onYouTubePlayerExitFullScreen() {

            }
        })

        movieDetailViewModel.movieTvPosterList.observe(this) {
            when(val data = it){

                is MoviePosterState.Empty -> {

                }

                is MoviePosterState.Loading -> {

                }

                is MoviePosterState.Success -> {

                    moviePosterAdapter.addAll(data.data)
                }


                is MoviePosterState.OnFail -> {

                }

                is MoviePosterState.Exception -> {

                }

                else -> {

                }
            }
        }

        movieDetailViewModel.movieTvTrailerList.observe(this){
            when(val data = it){
                MovieTrailerState.Empty -> {

                }
                is MovieTrailerState.Exception -> {

                }
                MovieTrailerState.Loading -> {

                }
                is MovieTrailerState.OnFail -> {

                }
                is MovieTrailerState.Success -> {

                    if(data.data.isNotEmpty()){
                        val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {

                                val defaultPlayerUiController = DefaultPlayerUiController(movieDetailBinding.youtubePlayerView, youTubePlayer)
                                movieDetailBinding.youtubePlayerView.setCustomPlayerUi(defaultPlayerUiController.rootView)
                                youtubeVideoId = data.data.first().key
                                youTubePlayer.cueVideo(data.data.first().key, 0F)
                            }
                        }

                        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
                        movieDetailBinding.youtubePlayerView.initialize(listener, options)
                    }else{
                        movieDetailBinding.tvTrailer.visibility = View.GONE
                        movieDetailBinding.youtubePlayerView.visibility = View.GONE
                    }


                }
            }
        }


    }

    private fun onClickPhotoDetail(context: Context, view: View, poster: String){
        val intent = Intent(context, PhotoDetailViewActivity::class.java).apply {
            putExtra("poster", poster)
        }
        val pair = Pair.create(view, "img_poster")
        val option = ActivityOptions.makeSceneTransitionAnimation(this@MovieDetailActivity, pair)
        startActivity(intent, option.toBundle())
    }




    override fun onDestroy() {
        movieDetailBinding.youtubePlayerView.release()
        super.onDestroy()
    }

    private fun <T : Parcelable> getParcelableExtra(intent: Intent, key: String, m_class: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(key, m_class)!!
        else
            intent.getParcelableExtra(key) as? T
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}