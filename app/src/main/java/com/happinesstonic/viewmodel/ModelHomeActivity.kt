package com.happinesstonic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.happinesstonic.utils.TextClassBinder
import com.happinesstonic.Event


class ModelHomeActivity: ViewModel() {

    val message = MutableLiveData<String>()//or Any(in place of String)

    fun myMessage(msg: String) {
        message.value = msg
    }

    val message2 = MutableLiveData<Event<String>>()

    fun myMessage2(msg: String) {
        message2.value = Event(msg)
    }

    val navigateToMediaItem: LiveData<Event<String>> get() = _navigateToMediaItem
    private val _navigateToMediaItem = MutableLiveData<Event<String>>()
    fun navigateToMediaItem(msg: String) {
        _navigateToMediaItem.value = Event(msg)
    }

    val textBinder: LiveData<Event<List<TextClassBinder>>> get() = _text_binder
    private val _text_binder = MutableLiveData<Event<List<TextClassBinder>>>()
    fun textBinder(msg: String) {
        val textBinders = mutableListOf<TextClassBinder>()
        _text_binder.value = Event(textBinders)
    }







    val refreshJokeType = MutableLiveData<Event<String>>()
    fun setRefreshJokeTab(j_type: String) {
        refreshJokeType.value = Event(j_type)
    }
    fun getRefreshJokeTab():LiveData<Event<String>> {
        return refreshJokeType
    }



    //text refresh
    val fragRefreshJokeText: LiveData<Event<String>> get() = _fragRefreshJokeText
    private val _fragRefreshJokeText = MutableLiveData<Event<String>>()
    fun fragRefreshJokeTabText(j_type: String) {
        _fragRefreshJokeText.value = Event(j_type)
    }
    //Image refresh
    val fragRefreshJokeImage: LiveData<Event<String>> get() = _fragRefreshJokeImage
    private val _fragRefreshJokeImage = MutableLiveData<Event<String>>()
    fun fragRefreshJokeTabImage(j_type: String) {
        _fragRefreshJokeImage.value = Event(j_type)
    }
    //Video refresh
    val fragRefreshJokeVideo: LiveData<Event<String>> get() = _fragRefreshJokeVideo
    private val _fragRefreshJokeVideo = MutableLiveData<Event<String>>()
    fun fragRefreshJokeTabVideo(j_type: String) {
        _fragRefreshJokeVideo.value = Event(j_type)
    }



    val downloadJokeFile: LiveData<Event<TextClassBinder>> get() = _downloadJokeFile
    private val _downloadJokeFile = MutableLiveData<Event<TextClassBinder>>()
    fun setDownloadJokeFile(data: TextClassBinder) {
        _downloadJokeFile.value = Event(data)
    }


}