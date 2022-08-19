package com.huyhieu.mydocuments.ui.fragments.steps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import javax.inject.Singleton

@FragmentScoped
class StepsVM @Inject constructor() : ViewModel() {
    var stepsLiveData: MutableLiveData<Int> = MutableLiveData()

    fun setStep(step: Int){
        stepsLiveData.postValue(step)
    }
}