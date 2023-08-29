package com.huyhieu.mydocuments.ui.fragments.tiktok

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.huyhieu.mydocuments.base.BaseVM
import javax.inject.Inject

class TikTokVM @Inject constructor() : BaseVM() {
    private val _currentPosition = MutableLiveData<Long?>(null)
    val currentPosition: LiveData<Long?> = _currentPosition

    fun setSeekToProgressBar(time: Long? = 0) {
        _currentPosition.postValue(time)
    }
}