package com.yeyint.movie_collection.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.yeyint.movie_collection.R
import com.yeyint.movie_collection.adapter.MovieListAdapter
import com.yeyint.movie_collection.adapter.MoviePosterHeaderAdapter
import com.yeyint.movie_collection.adapter.MoviePosterHeaderAdapterListener
import com.yeyint.movie_collection.adapter.PagingLoadingStateAdapter
import com.yeyint.movie_collection.databinding.ActivityMainBinding
import com.yeyint.movie_collection.helper.MovieConstant
import com.yeyint.movie_collection.model.MovieModel
import com.yeyint.movie_collection.viewModel.UpcomingMovieState
import com.yeyint.movie_collection.viewModel.MovieViewModel
import com.yeyint.movie_collection.viewModel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    ViewPager.OnPageChangeListener, MoviePosterHeaderAdapterListener {

    private val viewModel: SplashViewModel by viewModels()
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var moviePosterHeaderAdapter: MoviePosterHeaderAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var movieType = MovieConstant.movie
    private var scrollPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.showSplash.value
        }
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        init()
    }

    private fun init(){
        setSupportActionBar(mainBinding.toolbar.toolbar)
        supportActionBar?.apply {
            title = "Movie Database"
            setDisplayHomeAsUpEnabled(false)
        }

        //init adapter and layout manager
        movieListAdapter = MovieListAdapter(this)
        moviePosterHeaderAdapter = MoviePosterHeaderAdapter(this)
        linearLayoutManager = LinearLayoutManager(applicationContext)

        val concatAdapter = ConcatAdapter(moviePosterHeaderAdapter, movieListAdapter.withLoadStateHeaderAndFooter(
            header = PagingLoadingStateAdapter(movieListAdapter::retry),
            footer = PagingLoadingStateAdapter(movieListAdapter::retry)
        ))


        mainBinding.apply {
            toolbar.toolbar.setNavigationIcon(R.mipmap.ic_launcher)
            swipeRefresh.setOnRefreshListener(this@MainActivity)

            errorLayout.retryButton.setOnClickListener {
                movieListAdapter.retry()
            }

            flbArrow.setOnClickListener {
                recyclerView.smoothScrollToPosition(0)
            }


            //init recycler view
            recyclerView.apply {
                layoutManager = linearLayoutManager
                setHasFixedSize(true)
                adapter = concatAdapter
                addOnScrollListener(object: OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        scrollPosition += dy
                        if(scrollPosition > 1500){
                            flbArrow.visibility = View.VISIBLE
                        }else{
                            flbArrow.visibility = View.GONE
                        }
                        Log.d("onScrolled", "onScrolled: $scrollPosition $dy")
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        Log.d("onScrollStateChanged", "onScrollStateChanged: ")
                    }
                })
            }
        }

        lifecycleScope.launch {

            movieListAdapter.loadStateFlow.collectLatest { loadState ->
                mainBinding.progressBar.isVisible = loadState.refresh is LoadState.Loading

                if (loadState.refresh is LoadState.Error) {

                    movieViewModel.getAllMovieAndTvFromDb(movieType, movieViewModel.searchKey.value!!)

                    movieViewModel.offlineMovieList.observe(this@MainActivity) {
                        lifecycleScope.launch {
                            movieListAdapter.submitData(PagingData.from(data = it))
                        }
                    }
                }
            }

        }


        lifecycleScope.launch {

            movieViewModel.movieList.collect{
                movieListAdapter.submitData(it)
            }

        }

        movieViewModel.upcomingMovieList.observe(this) {
            when (val data = it) {
                is UpcomingMovieState.Empty -> {

                }

                is UpcomingMovieState.Loading -> {

                }

                is UpcomingMovieState.Success -> {

                    moviePosterHeaderAdapter.addAll(data.data)
                }


                is UpcomingMovieState.OnFail -> {

                }

                is UpcomingMovieState.Exception -> {

                }

                else -> {

                }
            }
        }


    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val search = menu?.findItem(R.id.appSearchBar)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = if(movieType == MovieConstant.movie)"Search movie" else "Search TV show"

        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                //when press search button dismiss keyboard
                this@MainActivity.dismissKeyboard()

                movieViewModel.searchKey.value = p0

                lifecycleScope.launch {
                    if(movieType == MovieConstant.movie){
                        movieViewModel.searchMovie()

                        movieViewModel.searchMovieList.collect{
                            movieListAdapter.submitData(it)
                        }
                    }else{
                        movieViewModel.searchTv()

                        movieViewModel.searchTvList.collect{
                            movieListAdapter.submitData(it)
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        searchView.setOnCloseListener {

            searchView.queryHint = ""
            movieViewModel.searchKey.value = ""

            lifecycleScope.launch{
                if(movieType == MovieConstant.movie){
                    movieViewModel.movieList.collect{
                        movieListAdapter.submitData(it)
                    }
                }else{
                    movieViewModel.tvSeriesList.collect{
                        movieListAdapter.submitData(it)
                    }
                }
            }
            false
        }

        return super.onCreateOptionsMenu(menu)
    }

    //dismiss keyboard function
    fun Activity.dismissKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE ) as InputMethodManager
        if( inputMethodManager.isAcceptingText )
            if(this.currentFocus != null){
                inputMethodManager.hideSoftInputFromWindow( this.currentFocus!!.windowToken, 0)
            }

    }

    override fun onRefresh() {
        mainBinding.swipeRefresh.isRefreshing = false
        movieListAdapter.refresh()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        TODO("Not yet implemented")
    }

    override fun onPageSelected(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onPageScrollStateChanged(state: Int) {
        TODO("Not yet implemented")
    }

    override fun onMoviePosterHeaderItemClick(type: String) {
        movieType = type
        when (type) {
            MovieConstant.movie -> {
                lifecycleScope.launch {

                    movieListAdapter.submitData(PagingData.empty())

                    if(movieViewModel.searchKey.value!!.isEmpty()){
                        movieViewModel.movieList.collect{
                            movieListAdapter.submitData(it)
                        }
                    }else{
                        movieViewModel.searchMovie()
                        movieViewModel.searchMovieList.collect{
                            movieListAdapter.submitData(it)
                        }
                    }

                }
            }
            MovieConstant.tvSeries -> {
                lifecycleScope.launch {

                    movieListAdapter.submitData(PagingData.empty())

                    if(movieViewModel.searchKey.value!!.isEmpty()){
                        movieViewModel.tvSeriesList.collect{
                            movieListAdapter.submitData(it)
                        }
                    }else{
                        movieViewModel.searchTv()
                        movieViewModel.searchTvList.collect{
                            movieListAdapter.submitData(it)
                        }
                    }



                }
            }
        }
    }

    override fun onMoviePosterItemClick(movieModel: MovieModel) {
        val intent = Intent(applicationContext, MovieDetailActivity::class.java).apply {
            putExtra("obj", movieModel)
        }
        startActivity(intent)
    }
}