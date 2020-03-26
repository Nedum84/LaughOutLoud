package com.laughoutloud.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laughoutloud.TextClassBinder
import com.laughoutloud.Event


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







    val refreshJokeType: LiveData<Event<String>> get() = _refreshJokeType
    private val _refreshJokeType = MutableLiveData<Event<String>>()
    fun refreshJokeTab(j_type: String) {
        _refreshJokeType.value = Event(j_type)
    }



    val textBinderChange: LiveData<Event<TextClassBinder>> get() = _textBinderChange
    private val _textBinderChange = MutableLiveData<Event<TextClassBinder>>()
    fun textBinderChange(data: TextClassBinder) {
        _textBinderChange.value = Event(data)
    }


}