package com.huyhieu.mydocuments.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huyhieu.mydocuments.repository.remote.retrofit.ResponseData
import com.huyhieu.mydocuments.repository.remote.retrofit.ResultAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class FlowMapper @Inject constructor() : Repository() {
    fun <T> mapperFlow(
        viewModel: ViewModel,
        stateFlow: MutableStateFlow<ResultAPI<ResponseData<T>>?>,
        blockFlowRequestApi: (Flow<ResultAPI<ResponseData<T>>>.() -> Unit)? = null,
        requestApi: suspend () -> Response<ResponseData<T>>
    ) {
        stateFlow.value = null
        val flowRequestApi: Flow<ResultAPI<ResponseData<T>>> = flow {
            val resultApi = requestApi.invoke()
            if (resultApi.isSuccessful && resultApi.body() != null) {
                emit(ResultAPI.success(resultApi.body()!!))
            } else {
                emit(ResultAPI.error("API Wrong!", "API not working!"))
            }
        }.onStart {
            emit(ResultAPI.loading())
        }
        //Config flow
        if (blockFlowRequestApi != null) {
            blockFlowRequestApi.invoke(flowRequestApi)
        } else {
            flowRequestApi.flowOn(Dispatchers.IO)
        }
        //Get result
        viewModel.viewModelScope.launch {
            flowRequestApi.collectLatest {
                stateFlow.value = it
            }
        }
    }
}


