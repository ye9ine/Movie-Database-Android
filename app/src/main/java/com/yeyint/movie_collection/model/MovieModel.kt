package com.yeyint.movie_collection.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "movieTable")
data class MovieModel(
    @PrimaryKey
    val id: Int,
    @ColumnInfo
    val adult: Boolean?,
    @ColumnInfo
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @ColumnInfo
    @SerializedName("original_language")
    val originalLanguage: String?,
    @ColumnInfo
    @SerializedName("original_title")
    val originalTitle: String?,
    @ColumnInfo
    val overview: String?,
    @ColumnInfo
    val popularity: Double?,
    @ColumnInfo
    @SerializedName("poster_path")
    val posterPath: String?,
    @ColumnInfo
    @SerializedName("release_date")
    val releaseDate: String?,
    @ColumnInfo
    val title: String?,
    @ColumnInfo
    val video: Boolean?,
    @ColumnInfo
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @ColumnInfo
    @SerializedName("vote_count")
    val voteCount: Int?,
    @ColumnInfo
    @SerializedName("original_name")
    val originalName: String?,
    @ColumnInfo
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    //local field
    @ColumnInfo
    var type: String?
): Parcelable