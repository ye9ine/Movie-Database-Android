package com.yeyint.movie_collection.di

import android.content.Context
import androidx.room.Room
import com.yeyint.movie_collection.BuildConfig
import com.yeyint.movie_collection.apiService.ApiService
import com.yeyint.movie_collection.db.MovieDb
import com.yeyint.movie_collection.helper.MovieConstant
import com.yeyint.movie_collection.repository.MovieDetailRepositoryImpl
import com.yeyint.movie_collection.repository.MovieRepositoryImpl
import com.yeyint.movie_collection.repository.MovieRepositoryImpl_Factory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    class Interceptor: okhttp3.Interceptor {
        override fun intercept(chain: okhttp3.Interceptor.Chain): Response {
            val original: Request = chain.request()
            val originalHttpUrl: HttpUrl = original.url
            val maxAge = 60

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_TOKEN)
                .build()

            val requestBuilder: Request.Builder = original.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .url(url)
            val request: Request = requestBuilder.build()
            return chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun okHttpClient(): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpBuilder.addInterceptor(loggingInterceptor)
        }
        okHttpBuilder
            .addInterceptor(Interceptor())
            .readTimeout(25, TimeUnit.SECONDS)
            .connectTimeout(25, TimeUnit.SECONDS)

        return okHttpBuilder.build()
    }

    @Singleton
    @Provides
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun apiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun movieRepositoryImpl(apiService: ApiService, movieDb: MovieDb): MovieRepositoryImpl {
        return MovieRepositoryImpl(apiService = apiService, movieDb = movieDb)
    }

    @Singleton
    @Provides
    fun movieDetailRepositoryImpl(apiService: ApiService, movieDb: MovieDb): MovieDetailRepositoryImpl {
        return MovieDetailRepositoryImpl(apiService = apiService, movieDb = movieDb)
    }

    @Singleton
    @Provides
    fun getMovieDb(@ApplicationContext context: Context): MovieDb{
        return Room.databaseBuilder(context.applicationContext, MovieDb::class.java, "movie_db").build()
    }

}