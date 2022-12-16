package com.huyhieu.mydocuments.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huyhieu.library.utils.logDebug
import com.huyhieu.mydocuments.repository.FlowMapper
import com.huyhieu.mydocuments.repository.LiveDataMapper
import com.huyhieu.mydocuments.repository.remote.retrofit.APIService
import com.huyhieu.mydocuments.repository.remote.retrofit.ResponseData
import com.huyhieu.mydocuments.repository.remote.retrofit.ResponsePokeAPI
import com.huyhieu.mydocuments.repository.remote.retrofit.ResultAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

open class BaseVM : ViewModel() {
    @Inject
    lateinit var mapper: LiveDataMapper

    @Inject
    lateinit var mapperFlow: FlowMapper

    @Inject
    lateinit var apiService: APIService

    var loadingState: MutableLiveData<LoadingState<*>> = MutableLiveData()

    fun <T> launch(
        stateFlow: MutableStateFlow<ResultAPI<ResponseData<T>>?>,
        onResult: ((ResultAPI<ResponseData<T>>?) -> Unit)? = null
    ) {
        viewModelScope.launch {
            stateFlow.collectLatest { onResult?.invoke(it) }
        }
    }

    fun <T> mapperAsyncFlows(
        vararg apis: Pair<String, suspend () -> Response<ResponsePokeAPI<T>>>,
        isHandleOnEachApi: Boolean = false,
        //onResult: ((resp: Pair<String, ResponsePokeAPI<T>?>) -> Unit)? = null
        onResult: ((resp: MutableMap<String, ResponsePokeAPI<T>?>) -> Unit)? = null
    ) {
        //Get result form Data Source
        viewModelScope.launch {
            val mutableMapAPI = mutableMapOf<String, ResponsePokeAPI<T>?>()
            channelFlow {
                apis.forEachIndexed { idx, api ->
                    launch {
                        flow {
                            val resultApi = api.second.invoke()
                            if (resultApi.isSuccessful && resultApi.body() != null) {
                                emit(Pair(api.first, resultApi.body()))
                            } else {
                                emit(Pair(api.first, null))
                            }
                        }.flowOn(Dispatchers.IO).collectLatest {
                            send(it)
                        }
                    }
                }
            }.onStart {
                loadingState.postValue(LoadingState<T>(isLoading = true, data = null))
            }.catch {
                logDebug(it.message)
            }.onCompletion {
                onResult?.invoke(mutableMapAPI)
                loadingState.postValue(LoadingState(isLoading = false, data = it))
            }.flowOn(Dispatchers.IO).collectLatest {
                mutableMapAPI[it.first] = it.second
            }
        }
    }

    fun <T> mapperSyncFlows(
        vararg apis: Pair<String, suspend () -> Response<ResponsePokeAPI<T>>>,
        onResult: ((resp: Pair<String, ResponsePokeAPI<T>?>) -> Unit)? = null
    ) {
        //Get result form Data Source
        viewModelScope.launch {
            flow {
                apis.forEachIndexed { idx, api ->
                    val resultApi = api.second.invoke()
                    if (resultApi.isSuccessful && resultApi.body() != null) {
                        emit(Pair(api.first, resultApi.body()))
                    } else {
                        emit(Pair(api.first, null))
                    }
                }
            }.onStart {
                loadingState.postValue(LoadingState<T>(isLoading = true, data = null))
            }.catch {
                logDebug(it.message)
            }.flowOn(Dispatchers.IO).collectLatest {
                onResult?.invoke(it)
                loadingState.postValue(LoadingState(isLoading = false, data = it, apiKey = it.first))
            }
        }
    }
}