package com.yeyint.movie_collection.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yeyint.movie_collection.view.MovieDetailActivity
import com.yeyint.movie_collection.databinding.MovieItemBinding
import com.yeyint.movie_collection.glideModule.GlideApp
import com.yeyint.movie_collection.helper.MovieConstant
import com.yeyint.movie_collection.model.MovieModel
import android.util.Pair

class MovieListAdapter(private  val mcontext: Context) : PagingDataAdapter<MovieModel, MovieListAdapter.ViewHolder>(DataDifferntiator) {

    inner class ViewHolder(private val movieItemBinding: MovieItemBinding) : RecyclerView.ViewHolder(movieItemBinding.root){
        fun bind(movieModel: MovieModel){
            val context = movieItemBinding.root.context
            movieItemBinding.apply {
                tvTitle.text = movieModel.title ?: movieModel.originalName
                tvReleaseDate.text = movieModel.releaseDate ?: movieModel.firstAirDate
                tvOverview.text = movieModel.overview
                tvRating.text = String.format("%.1f", movieModel.voteAverage)

                GlideApp.with(context)
                    .load(MovieConstant.basePosterPath + movieModel.posterPath)
                    .into(imgPoster)



                linearItem.setOnClickListener{
                    val intent = Intent(it.context, MovieDetailActivity::class.java).apply {
                        putExtra("obj", movieModel)
                    }
                    val pair1 = Pair.create(tvTitle as View, "transition_title")
                    val pair2 = Pair.create(imgPoster as View, "img_poster")
                    val option = ActivityOptions.makeSceneTransitionAnimation(mcontext as Activity, pair1, pair2)
                    context.startActivity(intent, option.toBundle())
                }
            }
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieModel: MovieModel = getItem(position)!!
        holder.bind(movieModel)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val movieItemBinding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(movieItemBinding)
    }

    object DataDifferntiator : DiffUtil.ItemCallback<MovieModel>() {

        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }
    }

}
