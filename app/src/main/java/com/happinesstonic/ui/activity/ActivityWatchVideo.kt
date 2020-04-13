package com.happinesstonic.ui.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.happinesstonic.utils.ClassAlertDialog
import com.happinesstonic.R
import kotlinx.android.synthetic.main.activity_watch_video.*


class ActivityWatchVideo : AppCompatActivity() {
    private val TEST_URL_MP4 = "http://techslides.com/demos/sample-videos/small.mp4"
    private val TEST_URL_MP4_2 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    private val TEST_URL_MP3 =  "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"

    private lateinit var exoPlayer: SimpleExoPlayer
    private var playbackPosition = 0L
    private var playWhenReady = true
    private var currentWindow = 0
    private val bandwidthMeter by lazy {
        DefaultBandwidthMeter()
    }
    private val adaptiveTrackSelectionFactory by lazy {
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    }
    private val dashUrl = "http://rdmedia.bbc.co.uk/dash/ondemand/bbb/2/client_manifest-separate_init.mpd"

    lateinit var exoPause:ImageButton
    lateinit var exoPlay:ImageButton
    lateinit var exoFFWd:ImageButton//fast forward
    lateinit var exoRew:ImageButton//rewind
    lateinit var exoPrev:ImageButton
    lateinit var exoNext:ImageButton
    lateinit var exoVr:ImageButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_video)


        exoPause =  findViewById(R.id.exo_pause)
        exoPlay = findViewById(R.id.exo_play)
        exoPrev = findViewById(R.id.exo_prev)
        exoRew = findViewById(R.id.exo_rew)
        exoFFWd = findViewById(R.id.exo_ffwd)
        exoNext = findViewById(R.id.exo_next)
        exoVr = findViewById(R.id.exo_vr)
//        exo_ffwd = (ImageButton) findViewById(R.id.exo_shuffle);

        exoFFWd.setOnClickListener {
            ClassAlertDialog(this).toast("FF clicked...")
        }
    }
    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializeExoplayer()
            if (exoPlayerView != null) {
                exoPlayerView.onResume()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {// || AudioPlayer.player == null
            initializeExoplayer()
            if (exoPlayerView != null) {
                exoPlayerView.onResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            if (exoPlayerView != null) {
                exoPlayerView.onPause()
            }
            releaseExoplayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            if (exoPlayerView != null) {
                exoPlayerView.onPause()
            }
            releaseExoplayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayerView.overlayFrameLayout!!.removeAllViews()
    }


    private fun initializeExoplayer() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultTrackSelector(adaptiveTrackSelectionFactory),
            DefaultLoadControl()
        )
        //OR
//        exoPlayer = ExoPlayerFactory.newSimpleInstance(this)



//        prepareExoplayer()
        val uri = Uri.parse(TEST_URL_MP4_2)
        val mediaSource = buildMediaSource(uri)
        exoPlayer.playWhenReady = playWhenReady
        exoPlayer.seekTo(currentWindow, playbackPosition)
        exoPlayer.prepare(mediaSource, false, false)
//        exoPlayer.prepare(mediaSource)

        exoPlayerView.player = exoPlayer
//        exoPlayerView.setShutterBackgroundColor(Color.TRANSPARENT)
        exoPlayerView.requestFocus()
        exoPlayer.addListener(object: Player.EventListener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//                super.onPlayerStateChanged(playWhenReady, playbackState)
                val stateString: String
                stateString = when (playbackState) {
                    ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
                    ExoPlayer.STATE_BUFFERING -> {
                        progressBar.visibility = View.VISIBLE
                        "ExoPlayer.STATE_BUFFERING -"
                    }
                    ExoPlayer.STATE_READY -> {
                        progressBar.visibility = View.INVISIBLE
                        "ExoPlayer.STATE_READY     -"
                    }
                    ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
                    else -> "UNKNOWN_STATE             -"
                }
                Log.d("FragmentActivity.TAG", "changed state to " + stateString+ " playWhenReady: " + playWhenReady
                )
            }
        })
    }
    private fun releaseExoplayer() {
        if (exoPlayer != null) {
            playWhenReady = exoPlayer.playWhenReady
            playbackPosition = exoPlayer.currentPosition
            currentWindow = exoPlayer.currentWindowIndex
            exoPlayer.release()
        }
    }

    private fun buildMediaSourceForDash(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultHttpDataSourceFactory("ua", bandwidthMeter)
        val dashChunkSourceFactory = DefaultDashChunkSource.Factory(dataSourceFactory)
        return DashMediaSource(uri, dataSourceFactory, dashChunkSourceFactory, null, null)
//        OR
//        val dataSourceFactory: DataSource.Factory =DefaultDataSourceFactory(this, "exoplayer-codelab")
//        val mediaSourceFactory = DashMediaSource.Factory(dataSourceFactory)
//        return mediaSourceFactory.createMediaSource(uri)
    }

    private fun buildMediaSource2(uri: Uri): MediaSource? {//for mp3/mp4
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(this, "exoplayer-codelab")
        val mediaSourceFactory = DashMediaSource.Factory(dataSourceFactory)
        return mediaSourceFactory.createMediaSource(uri)
    }
    private fun buildMediaSource(uri: Uri): MediaSource {
        val userAgent = "exoplayer-codelab"

        if (uri.lastPathSegment!!.contains("mp3") || uri.lastPathSegment!!.contains("mp4")) {//for mp3/mp4
            val mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))
            return  ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri)
        } else if (uri.lastPathSegment!!.contains("m3u8")) {// for HLS
            return HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else {//For dash
            val dashChunkSourceFactory = DefaultDashChunkSource.Factory(DefaultHttpDataSourceFactory("ua", bandwidthMeter))
            val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
            return DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri)
        }
    }
    private fun prepareExoplayer() {
        val uri = Uri.parse(dashUrl)
        val mediaSource = buildMediaSource(uri)
        exoPlayer.playWhenReady = playWhenReady
        exoPlayer.seekTo(currentWindow, playbackPosition)
        exoPlayer.prepare(mediaSource, false, false)
//        exoPlayer.prepare(mediaSource)
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUiFullScreen()
        } else {
            hideSystemUi()
        }
    }
    //to hide system UI
    @SuppressLint("InlinedApi")
    private fun hideSystemUiFullScreen() {
        exoPlayerView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        exoPlayerView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }











//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_watch_video)
////        andExoPlayerView = findViewById(R.id.andExoPlayerView)
////        findViewById<View>(R.id.local).setOnClickListener { selectLocaleVideo() }
////        findViewById<View>(R.id.mp4).setOnClickListener { loadMP4ServerSide() }
////        findViewById<View>(R.id.hls).setOnClickListener { loadHls() }
////        findViewById<View>(R.id.mp3).setOnClickListener { loadMp3() }
//
//        mpw_video_player.startPlay(TEST_URL_MP4, MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "video name");
//    }

//    private fun loadMp3() {
//        andExoPlayerView!!.setSource(TEST_URL_MP3)
//    }
//
//    private fun loadHls() {
//        andExoPlayerView!!.setSource(TEST_URL_HLS)
//    }
//
//    private fun loadMP4ServerSide() {
//        andExoPlayerView!!.setSource(TEST_URL_MP4)
//    }
//
//    private fun selectLocaleVideo() {
//        if (PublicFunctions.checkAccessStoragePermission(this)) {
//            val intent = Intent()
//            intent.type = "video/*"
//            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
//            intent.action = Intent.ACTION_GET_CONTENT
//            startActivityForResult(Intent.createChooser(intent, "Select Video"), req_code)
//        }
//    }
//
//    private fun loadMP4Locale(filePath: String?) {
////        andExoPlayerView!!.setSource(filePath)
//        mpw_video_player.startPlay(filePath, MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "video name");
//    }
//
//    override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?
//    ) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == req_code && resultCode == Activity.RESULT_OK) {
//            val finalVideoUri: Uri? = data!!.data
//            var filePath: String? = null
//            try {
//                filePath = PathUtil.getPath(this, finalVideoUri)
//                loadMP4Locale(filePath)
//            } catch (e: URISyntaxException) {
//                e.printStackTrace()
//                Toast.makeText(this, "Failed: " + e.message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}

