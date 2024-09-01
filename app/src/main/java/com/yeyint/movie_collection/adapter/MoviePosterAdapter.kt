package com.yeyint.movie_collection.adapter
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Pair
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yeyint.movie_collection.view.PhotoDetailViewActivity
import com.yeyint.movie_collection.databinding.PosterItemBinding
import com.yeyint.movie_collection.glideModule.GlideApp
import com.yeyint.movie_collection.helper.MovieConstant
import com.yeyint.movie_collection.model.MoviePosterModel

private var posterList: List<MoviePosterModel> = emptyList()

class MoviePosterAdapter(private val mcontext: Context) : RecyclerView.Adapter<MoviePosterAdapter.ViewHolder>() {

    inner class ViewHolder(private val posterItemBinding: PosterItemBinding) : RecyclerView.ViewHolder(posterItemBinding.root){
        fun bind(moviePosterModel: MoviePosterModel){
            val context = posterItemBinding.root.context

            posterItemBinding.apply {

                GlideApp.with(context)
                    .load(MovieConstant.basePosterPath + moviePosterModel.file_path)
                    .into(imgPoster)

                imgPoster.setOnClickListener {
                    val intent = Intent(it.context, PhotoDetailViewActivity::class.java).apply {
                        putExtra("poster", moviePosterModel.file_path)
                    }
                    val pair = Pair.create(imgPoster as View, "img_poster")
                    val option = ActivityOptions.makeSceneTransitionAnimation(mcontext as Activity, pair)
                    context.startActivity(intent, option.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val posterItemBinding = PosterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(posterItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val moviePosterModel: MoviePosterModel = posterList[position]
        holder.bind(moviePosterModel)
    }

    override fun getItemCount(): Int {
        return posterList.size
    }

    fun addAll(list: List<MoviePosterModel>){
        posterList = list
        notifyDataSetChanged()
    }

    fun clear(){
        posterList = emptyList()
        notifyDataSetChanged()
    }

}