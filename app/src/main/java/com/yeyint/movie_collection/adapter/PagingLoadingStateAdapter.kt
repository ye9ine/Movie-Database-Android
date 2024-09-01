package com.yeyint.movie_collection.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yeyint.movie_collection.R
import com.yeyint.movie_collection.databinding.LoadStateItemBinding
import com.yeyint.movie_collection.databinding.MovieItemBinding

class PagingLoadingStateAdapter(private val retryCallback: () -> Unit): LoadStateAdapter<PagingLoadingStateAdapter.ViewHolder>() {


    class ViewHolder(private val binding: LoadStateItemBinding, private val retryCallback: () -> Unit): RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible =
                    !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
                errorMsg.text = (loadState as? LoadState.Error)?.error?.message

                retryButton.setOnClickListener {
                    retryCallback()
                }
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) = holder.bind(loadState)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = ViewHolder(
        LoadStateItemBinding.bind(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_item, parent, false)
        ), retryCallback = retryCallback
    )
}