package com.yeyint.movie_collection.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "upcomingMovieTable")
data class UpcomingMovieModel(
    @PrimaryKey
    val id: Int,
    @ColumnInfo
    val adult: Boolean?,
    @ColumnInfo
    val backdropPath: String?,
    @ColumnInfo
    val originalLanguage: String?,
    @ColumnInfo
    val originalTitle: String?,
    @ColumnInfo
    val overview: String?,
    @ColumnInfo
    val popularity: Double?,
    @ColumnInfo
    val posterPath: String?,
    @ColumnInfo
    val releaseDate: String?,
    @ColumnInfo
    val title: String?,
    @ColumnInfo
    val video: Boolean?,
    @ColumnInfo
    val voteAverage: Double?,
    @ColumnInfo
    val voteCount: Int?,
    @ColumnInfo
    val originalName: String?,
    @ColumnInfo
    val firstAirDate: String?,
    //local field
    @ColumnInfo
    var type: String?
): Parcelable