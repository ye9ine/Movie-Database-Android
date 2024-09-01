package com.yeyint.movie_collection.model

import com.google.gson.annotations.SerializedName

data class MoviePosterResultModel(
    val id: Int,
    val backdrops: List<MoviePosterModel>,
    val logos: List<MoviePosterModel>,
    val posters: ArrayList<MoviePosterModel>,
)
