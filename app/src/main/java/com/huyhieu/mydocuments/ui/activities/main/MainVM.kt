package com.huyhieu.mydocuments.ui.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class MainVM @Inject constructor(): ViewModel() {
    var pushNotify: MutableLiveData<String> = MutableLiveData("")
    fun pushMessage(msg: String){
        pushNotify.postValue(msg)
    }
}