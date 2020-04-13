package com.happinesstonic.ui.fragment


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import com.happinesstonic.*
import com.happinesstonic.BuildConfig
import com.happinesstonic.R
import com.happinesstonic.utils.*

import com.happinesstonic.viewmodel.ModelHomeActivity
import kotlinx.android.synthetic.main.custom_playback_control2.*
import kotlinx.android.synthetic.main.fragment_dialog_watch_video.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.HashMap


class FragmentDialogWatchVideo : DialogFragment() {
    lateinit var thisContext: Activity
    lateinit var modelHomeActivity: ModelHomeActivity
    lateinit var listDataBinder: TextClassBinder
    lateinit var allListDataBinder: MutableList<TextClassBinder>


    private var exoPlayer: SimpleExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private var currentWindow = 0
    private val bandwidthMeter by lazy {
        DefaultBandwidthMeter()
    }
    private val adaptiveTrackSelectionFactory by lazy {
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    }

    lateinit var exoPause: ImageButton
    lateinit var exoPlay: ImageButton
    lateinit var exoFFWd: ImageButton//fast forward
    lateinit var exoRew: ImageButton//rewind
    lateinit var exoPrev: ImageButton
    lateinit var exoNext: ImageButton


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_watch_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisContext = activity!!

        exoPause =  exo_pause
        exoPlay = exo_play
        exoPrev = exo_prev_custom
        exoRew = exo_rew
        exoFFWd = exo_ffwd
        exoNext = exo_next_custom

        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            thisContext,
            DefaultTrackSelector(adaptiveTrackSelectionFactory),
            DefaultLoadControl()
        )
        //OR
//        exoPlayer = ExoPlayerFactory.newSimpleInstance(this)

        val data = Gson().fromJson(
            ClassSharedPreferences(
                thisContext
            ).getCurrentDataBinder(), Array<TextClassBinder>::class.java).asList()
        allListDataBinder = (Gson().fromJson(
            ClassSharedPreferences(
                thisContext
            ).getCurrentListBinder(), Array<TextClassBinder>::class.java).asList()).toMutableList()
        allListDataBinder.sortByDescending { it.joke_id }
        if (data.isEmpty() ||data[0].joke_type !="video"){
            dialog!!.dismiss()
            ClassAlertDialog(thisContext).toast("Error occurred, Try again...")
            return
        }
        listDataBinder = data[0]





        exoPrev.setOnClickListener {
            loadPrevVideo()
        }
        exoNext.setOnClickListener {
            loadNextVideo()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.statusBarColor = Color.parseColor("#000000")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.statusBarColor = ContextCompat.getColor(thisContext, R.color.colorPrimaryDark)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        modelHomeActivity = activity!!.run{
            ViewModelProvider(this).get(ModelHomeActivity::class.java)
        }


        if ((ClassSharedPreferences(context)
                .isUserAdmin()||listDataBinder.joke_user_id == ClassSharedPreferences(
                context
            ).getUserId()))
            img_delete_btn.visibility = View.VISIBLE else img_delete_btn.visibility = View.GONE

        go_back.setOnClickListener {
            dialog!!.dismiss()
        }
        img_whatsapp_share.setOnClickListener{
            shareVideo(listDataBinder, "whatsapp")
        }
        img_share_btn.setOnClickListener{
            shareVideo(listDataBinder, "others")
        }
        img_delete_btn.setOnClickListener{
            AlertDialog.Builder(context)
                .setMessage("Delete this Video?")
                .setPositiveButton("Delete"
                ) { _, _ ->
                    delPost(listDataBinder)
                }.setNegativeButton("cancel"
                ) { _, _ ->
                }.show()
        }
    }

    private fun shareVideo(listDetail: TextClassBinder, shareTo:String){
        val fileName = ClassUtilities().getDirPath(thisContext, "vid")+"/${ClassUtilities().getVideoName(listDetail)}"

        if(File(fileName).exists()){
            ClassShareApp(thisContext)
                .shareFileFromPath(fileName,"$shareTo")
        }else{
            listDetail.share_to = shareTo
            modelHomeActivity.setDownloadJokeFile(listDetail)
        }

    }

    private fun delPost(listDetail: TextClassBinder, position: Int=1) {
        val pDialog = ClassProgressDialog(context)
        pDialog.createDialog()

        val stringRequest = object : StringRequest(
            Method.POST, UrlHolder.URL_EDIT_JOKE,
            com.android.volley.Response.Listener { response ->
                pDialog.dismissDialog()

                try {
                    val obj = JSONObject(response)
                    val responseStatus = obj.getString("message")
                    if (responseStatus =="ok") {

                        dialog!!.dismiss()
                    }else{
                        ClassAlertDialog(thisContext).toast(responseStatus)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            com.android.volley.Response.ErrorListener { _ ->
                pDialog.dismissDialog()
                ClassAlertDialog(thisContext).toast("Post couldn't be deleted (NETWORK ERROR!)")
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val params = HashMap<String, String?>()
                params["request_type"] = "delete_post"
                params["joke_id"] = "${listDetail.joke_id}"
                return params
            }
        }
        //adding request to queue
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
        //volley interactions end
    }






    private fun initializeExoplayer() {
        exoPrev.isEnabled = allListDataBinder.first() != listDataBinder

        val curVidFileName = ClassUtilities().getDirPath(thisContext, "vid")+"/${ClassUtilities().getVideoName(listDataBinder)}"

        val uri = if(File(curVidFileName).exists()){
            FileProvider.getUriForFile(
                thisContext,
                BuildConfig.APPLICATION_ID + ".provider",
                File(curVidFileName))
        }else{
            Uri.parse(listDataBinder.joke_video_url)
        }

//        val uri = Uri.parse(listDataBinder.joke_video_url)
        val mediaSource = buildMediaSource(uri)
        exoPlayer?.playWhenReady = playWhenReady
        exoPlayer?.seekTo(currentWindow, playbackPosition)
        exoPlayer?.prepare(mediaSource, false, false)
//        exoPlayer.prepare(mediaSource)

        exoPlayerView?.player = exoPlayer
//        exoPlayerView.setShutterBackgroundColor(Color.TRANSPARENT)
        exoPlayerView?.requestFocus()
        progressBar.visibility = View.VISIBLE
        exoPlayer?.addListener(object: Player.EventListener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//                super.onPlayerStateChanged(playWhenReady, playbackState)
                val stateString:String
                 when (playbackState) {
                    ExoPlayer.STATE_IDLE -> stateString = "ExoPlayer.STATE_IDLE      -"
                    ExoPlayer.STATE_BUFFERING -> {
                        progressBar.visibility = View.VISIBLE
                        stateString = "ExoPlayer.STATE_BUFFERING -"
                    }
                    ExoPlayer.STATE_READY -> {
                        progressBar.visibility = View.INVISIBLE
                        stateString = "ExoPlayer.STATE_READY     -"
                    }
                    ExoPlayer.STATE_ENDED -> {
                        loadNextVideo()
                        stateString = "ExoPlayer.STATE_ENDED     -"
                    }
                    else -> stateString = "UNKNOWN_STATE             -"
                }
                Log.d("FragmentActivity.TAG","changed state to $stateString playWhenReady: $playWhenReady")
            }
        })
    }
    fun loadPrevVideo(){
        if (allListDataBinder.first() != listDataBinder){
            val curIndex = allListDataBinder.indexOf(listDataBinder)
            listDataBinder = allListDataBinder[curIndex-1]
            initializeExoplayer()
        }
    }
    fun loadNextVideo(){
        if (allListDataBinder.last() == listDataBinder){
            fetchFromServer()
        }else{
            val curIndex = allListDataBinder.indexOf(listDataBinder)
            listDataBinder = allListDataBinder[curIndex+1]
            initializeExoplayer()
        }
    }

    private fun fetchFromServer() {
        val start_page_from = if(allListDataBinder!!.size==0){0}else{allListDataBinder!!.minBy { it.joke_id }!!.joke_id}
        val pDialog = ClassProgressDialog(
            thisContext,
            "Getting more videos, Please wait..."
        )
        pDialog.createDialog()
        val stringRequest = object : StringRequest(Method.POST, UrlHolder.URL_GET_TEXT_JOKE,
            Response.Listener { response ->
                pDialog.dismissDialog()

                try {
                    val obj = JSONObject(response)
                    if (!obj.getBoolean("error")) {
                        val jsonResponse = obj.getJSONArray("joke_arrayszxz")

                        if ((jsonResponse.length()!=0)){
                            val newDataArray = mutableListOf<TextClassBinder>()
                            for (i in 0 until jsonResponse.length()) {
                                val jsonObj = jsonResponse.getJSONObject(i)
                                val nData =
                                    TextClassBinder(
                                        jsonObj.getInt("joke_id"),
                                        jsonObj.getString("joke_cat"),
                                        jsonObj.getString("joke_type"),
                                        jsonObj.getString("joke_text"),
                                        jsonObj.getString("joke_image_url"),
                                        jsonObj.getString("joke_video_url"),
                                        jsonObj.getString("joke_time"),
                                        jsonObj.getString("joke_no_of_like"),
                                        jsonObj.getString("joke_no_of_comment"),
                                        jsonObj.getString("joke_user_id"),
                                        jsonObj.getBoolean("is_already_liked")
                                    )
                                if (nData !in allListDataBinder!!)newDataArray.add(nData)
                            }
                            allListDataBinder.addAll(newDataArray)
                            allListDataBinder = (allListDataBinder.distinctBy { it.joke_id }).toMutableList()
                            allListDataBinder.sortByDescending { it.joke_id }
                            loadNextVideo()

                        }else{
                            ClassAlertDialog(thisContext).toast("No more video...")
                        }
                    } else {
                        ClassAlertDialog(thisContext).toast("An error occurred, try again...")
                        dialog!!.dismiss()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { _er ->
                _er.printStackTrace()
                pDialog.dismissDialog()

                ClassAlertDialog(thisContext).toast("No internet connection...")

            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val params = HashMap<String, String?>()
                params["request_type"] = "get_jokes"
                params["joke_type"] = "video"
                params["start_page_from"] = "$start_page_from"
                params["user_id"] = ClassSharedPreferences(
                    thisContext
                ).getUserId()
                return params
            }
        }

        VolleySingleton.instance?.addToRequestQueue(stringRequest)//adding request to queue
    }

    private fun releaseExoplayer() {
        if (exoPlayer != null) {
            playWhenReady = exoPlayer?.playWhenReady!!
            playbackPosition = exoPlayer?.currentPosition!!
            currentWindow = exoPlayer?.currentWindowIndex!!
            exoPlayer?.release()
        }
    }


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializeExoplayer()
            if (exoPlayerView != null) {
                exoPlayerView?.onResume()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {// || AudioPlayer.player == null
            initializeExoplayer()
            if (exoPlayerView != null) {
                exoPlayerView?.onResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            if (exoPlayerView != null) {
                exoPlayerView?.onPause()
            }
            releaseExoplayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            if (exoPlayerView != null) {
                exoPlayerView?.onPause()
            }
            releaseExoplayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayerView?.overlayFrameLayout?.removeAllViews()
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val userAgent = "exoplayer-codelab"

        if (uri.lastPathSegment!!.contains("mp3") || uri.lastPathSegment!!.contains("mp4")) {//for mp3/mp4
            val mediaDataSourceFactory = DefaultDataSourceFactory(thisContext, Util.getUserAgent(thisContext, "mediaPlayerSample"))
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








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        } else {
            setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_NoActionBar)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
//        dialog.window!!.attributes.windowAnimations = R.style.Animation_WindowSlideUpDown
//        isCancelable = false
        return dialog
    }
}

