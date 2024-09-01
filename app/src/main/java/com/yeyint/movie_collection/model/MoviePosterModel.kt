package com.yeyint.movie_collection.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "movie_poster")
data class MoviePosterModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo
    val movieId: Int,

    @ColumnInfo
    val aspect_ratio: Double,

    @ColumnInfo
    val file_path: String,

    @ColumnInfo
    val height: Int,

    @ColumnInfo
    val iso_639_1: String?,

    @ColumnInfo
    val vote_average: Double,

    @ColumnInfo
    val vote_count: Int,

    @ColumnInfo
    val width: Int
): Parcelable