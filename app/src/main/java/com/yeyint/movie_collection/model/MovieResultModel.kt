package com.yeyint.movie_collection.model

import com.google.gson.annotations.SerializedName

data class MovieResultModel(
    val page: Int,
    val results: ArrayList<MovieModel>,
    val total_pages: Int,
    val total_results: Int,
)
