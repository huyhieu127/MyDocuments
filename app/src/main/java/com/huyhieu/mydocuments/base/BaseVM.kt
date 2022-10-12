package com.huyhieu.mydocuments.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huyhieu.mydocuments.repository.remote.retrofit.ResponseData
import com.huyhieu.mydocuments.repository.remote.retrofit.ResultAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

open class BaseVM : ViewModel() {
    fun <T> launch(
        stateFlow: MutableStateFlow<ResultAPI<ResponseData<T>>?>,
        onResult: ((ResultAPI<ResponseData<T>>?) -> Unit)? = null
    ) {
        viewModelScope.launch {
            stateFlow.collectLatest { onResult?.invoke(it) }
        }
    }
}