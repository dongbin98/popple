package com.dongbin.popple.ui.main.ui.heart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HeartViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Heart Fragment"
    }
    val text: LiveData<String> = _text
}