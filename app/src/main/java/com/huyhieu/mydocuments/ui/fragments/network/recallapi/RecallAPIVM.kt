package com.huyhieu.mydocuments.ui.fragments.network.recallapi

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.huyhieu.mydocuments.base.BaseVM
import javax.inject.Inject

class RecallAPIVM @Inject constructor() : BaseVM() {
    val userLD: MutableLiveData<JsonObject?> = MutableLiveData()
    fun fetchUser() {
        flowMapper.executeAPI(
            apiService = {
                reqResApiService.getUser()
            },
            onLoading = {
                showLoading()
            },
            onCompletion = {
                hideLoading()
            },
        ) {
            val data = it?.getAsJsonObject("data")
            userLD.postValue(data)
        }
    }
}