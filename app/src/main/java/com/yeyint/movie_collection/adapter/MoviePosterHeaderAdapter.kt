package com.yeyint.movie_collection.adapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.Indicators.PagerIndicator
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import com.yeyint.movie_collection.R
import com.yeyint.movie_collection.databinding.MoviePosterHeaderBinding
import com.yeyint.movie_collection.helper.MovieConstant
import com.yeyint.movie_collection.helper.RoundCornerTransform
import com.yeyint.movie_collection.model.MovieModel

interface MoviePosterHeaderAdapterListener {
    fun onMoviePosterHeaderItemClick(type: String)
    fun onMoviePosterItemClick(movieModel: MovieModel)
}

private var movieType: String = MovieConstant.movie
private var posterList: List<MovieModel> = emptyList()

class MoviePosterHeaderAdapter(private val moviePosterHeaderAdapterListener: MoviePosterHeaderAdapterListener) : RecyclerView.Adapter<MoviePosterHeaderAdapter.ViewHolder>(){

    inner class ViewHolder(private val moviePosterHeaderBinding: MoviePosterHeaderBinding) : RecyclerView.ViewHolder(moviePosterHeaderBinding.root),
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
        val context = moviePosterHeaderBinding.root.context

        fun bind(){

            moviePosterHeaderBinding.apply {

                if(movieType == MovieConstant.movie){
                    tvInTheaters.background = ContextCompat.getDrawable(context, R.drawable.pill_shape_background)
                }else{
                    tvOnTv.background = ContextCompat.getDrawable(context, R.drawable.pill_shape_background)
                }

                tvInTheaters.setOnClickListener {
                    tvInTheaters.background = ContextCompat.getDrawable(context, R.drawable.pill_shape_background)
                    tvOnTv.background = null
                    moviePosterHeaderAdapterListener.onMoviePosterHeaderItemClick(MovieConstant.movie)
                    movieType = MovieConstant.movie
                }

                tvOnTv.setOnClickListener {
                    tvOnTv.background = ContextCompat.getDrawable(context, R.drawable.pill_shape_background)
                    tvInTheaters.background = null
                    moviePosterHeaderAdapterListener.onMoviePosterHeaderItemClick(MovieConstant.tvSeries)
                    movieType = MovieConstant.tvSeries
                }

                sliderLayout.removeAllSliders()
                for(i in posterList){
                    val textSliderView = TextSliderView(context)

                    textSliderView.apply {
                        description(i.title)
                        image(MovieConstant.baseBackdropPath + i.backdropPath)
                        scaleType = BaseSliderView.ScaleType.CenterCrop
                        setOnSliderClickListener(this@ViewHolder)
                        bundle(Bundle())
                        bundle.putParcelable("obj", i)
                    }

                    sliderLayout.apply {
                        addSlider(textSliderView)
                        indicatorVisibility = PagerIndicator.IndicatorVisibility.Invisible
                        setCustomAnimation(DescriptionAnimation())
                        setDuration(5000)
                        addOnPageChangeListener(this@ViewHolder)
                    }
                }

            }
        }

        override fun onSliderClick(slider: BaseSliderView?) {
            val model = slider?.bundle?.get("obj") as MovieModel
            model.type = MovieConstant.movie
            moviePosterHeaderAdapterListener.onMoviePosterItemClick(model)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int,
        ) {

        }

        override fun onPageSelected(position: Int) {

        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    fun addAll(list: List<MovieModel>){
        posterList = list
        notifyDataSetChanged()
    }

    fun clear(){
        posterList = emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val posterItemBinding = MoviePosterHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(posterItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val moviePosterModel: MoviePosterHeaderBinding = this.list[position]
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 1
    }

}