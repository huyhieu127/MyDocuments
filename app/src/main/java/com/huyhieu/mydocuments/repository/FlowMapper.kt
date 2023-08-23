package com.huyhieu.mydocuments.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huyhieu.mydocuments.libraries.utils.logDebug
import com.huyhieu.mydocuments.libraries.utils.logError
import com.huyhieu.mydocuments.repository.remote.retrofit.ResponsePokeAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class FlowMapper @Inject constructor() : Repository() {
    fun <T> mapperFlow(
        viewModel: ViewModel,
        stateFlow: MutableStateFlow<ResponsePokeAPI<T>?>,
        blockFlowRequestApi: (Flow<ResponsePokeAPI<T>>.() -> Unit)? = null,
        requestApi: suspend () -> Response<ResponsePokeAPI<T>>
    ) {
        /* stateFlow.value = null
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
        }*/
        viewModel.viewModelScope.launch {
            stateFlow.value = null
            flow {
                val resultAPI = requestApi.invoke()
                if (resultAPI.isSuccessful && resultAPI.body() != null) {
                    emit(resultAPI.body())
                } else {
                    emit(null)
                }
            }.flowOn(Dispatchers.IO).onStart {
                emit(null)
            }.catch {
                logError("FLOW: ${it.message}")
            }.collectLatest {
                //stateFlow.value = it
                logDebug("collect: ${it?.results}")
            }
        }

    }
}


