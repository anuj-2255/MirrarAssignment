package com.example.testassignment.view

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.testassignment.*
import com.example.testassignment.databinding.ActivityHomeBinding
import com.example.testassignment.model.bean.NasaResponse
import com.example.testassignment.utils.showToast
import com.example.testassignment.view.navigator.HomeNavigator
import com.example.testassignment.view_model.HomeViewModel
import com.google.gson.Gson
import com.happytaxidriver.view.base.BaseActivity
import com.orhanobut.hawk.Hawk
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(HomeViewModel::class),
    HomeNavigator {

    override val bindingVariable: Int get() = BR.viewModel
    override val layoutId: Int get() = R.layout.activity_home
    override val viewModel: HomeViewModel by viewModel()
    private var player: ExoPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        initControl()
    }

    private fun initControl() {
        //using shared preference for caching because response of api has not much data and can be stored in shared preference.
        //this will set the old data when ever our api failed to new data
        if (Hawk.contains(NASA_RESPONSE)) {
            val data = Hawk.get<String>(NASA_RESPONSE)
            viewModel.nasaObj = Gson().fromJson(data, NasaResponse::class.java)
            showHidePlayerView()
        }

        //api call to replace and load new data in sharedPreference whenever we open our app
        viewModel.getDailyImage()

        //calling api on refresh by swiping down on scree.
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getDailyImage()
            //setting this false to avoid conflict with out loader.
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    // to show toast
    override fun showMsg(msg: String) {
        showToast(msg)
    }

    //this function check whether to show ImageView or Exoplayer on Runtime.
    override fun showHidePlayerView() {
        binding.apply {

            //checking media type and make view visible as per type.
            if (viewModel?.nasaObj?.media_type.equals(TYPE_IMAGE)) {
                ivDailyImage.visibility = VISIBLE
                playerView.visibility = GONE
            } else {
                playerView.visibility = VISIBLE
                ivDailyImage.visibility = GONE
                initExoPlayer()
            }
        }
    }

    //to initialize exoplayer
    private fun initExoPlayer() {
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player
        val mediaItem =
            MediaItem.fromUri(
                viewModel.nasaObj?.url
                    ?: PLACE_HOLDER_VIDEO
            )//adding a placeholder video for player to play in case of missing url.
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
    }
}