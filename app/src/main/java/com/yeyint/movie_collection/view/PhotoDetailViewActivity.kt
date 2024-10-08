package com.yeyint.movie_collection.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.yeyint.movie_collection.R
import com.yeyint.movie_collection.databinding.ActivityPhotoDetailViewBinding
import com.yeyint.movie_collection.glideModule.GlideApp
import com.yeyint.movie_collection.helper.MovieConstant

class PhotoDetailViewActivity : AppCompatActivity() {

    private lateinit var photoDetailViewBinding: ActivityPhotoDetailViewBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoDetailViewBinding = ActivityPhotoDetailViewBinding.inflate(layoutInflater)
        setContentView(photoDetailViewBinding.root)
        init()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun init() {
        val poster = intent.getStringExtra("poster")!!

        setSupportActionBar(photoDetailViewBinding.toolbar.toolbar)
        window.statusBarColor = getColor(R.color.black)
        photoDetailViewBinding.toolbar.toolbar.apply {
            setBackgroundColor(getColor(R.color.black))
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }

        //bind image to glide library
        GlideApp.with(applicationContext)
            .load(MovieConstant.basePosterHighResolutionPath + poster)
            .into(photoDetailViewBinding.imgPosterDetail)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}



