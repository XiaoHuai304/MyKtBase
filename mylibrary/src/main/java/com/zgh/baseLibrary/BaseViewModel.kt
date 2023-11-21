package com.zgh.baseLibrary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    companion object {
        var _showLoading = MutableLiveData<Boolean>().apply { value = false }
    }

    var showLoading: MutableLiveData<Boolean> = _showLoading

}