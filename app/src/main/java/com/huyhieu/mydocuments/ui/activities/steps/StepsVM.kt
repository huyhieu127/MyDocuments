package com.huyhieu.mydocuments.ui.activities.steps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class StepsVM @Inject constructor() : ViewModel() {
    val ldStep = MutableLiveData<Int>()

    init {
        ldStep.value = 1
    }
}