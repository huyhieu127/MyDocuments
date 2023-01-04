package com.huyhieu.mydocuments.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huyhieu.library.utils.logDebug
import com.huyhieu.library.utils.logError
import com.huyhieu.mydocuments.repository.FlowMapper
import com.huyhieu.mydocuments.repository.LiveDataMapper
import com.huyhieu.mydocuments.repository.remote.retrofit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

open class BaseVM : ViewModel() {

    @Inject
    lateinit var mapper: LiveDataMapper

    @Inject
    lateinit var mapperFlow: FlowMapper

    @Inject
    @Named("PokeAPI")
    lateinit var pokeApiService: PokeAPIService

    @Inject
    @Named("ReqResAPI")
    lateinit var reqResApiService: ReqResAPIService

    var loadingState: MutableLiveData<LoadingState<*>> = MutableLiveData()

    fun <T> launch(
        stateFlow: MutableStateFlow<ResultAPI<ResponseData<T>>?>,
        onResult: ((ResultAPI<ResponseData<T>>?) -> Unit)? = null
    ) {
        viewModelScope.launch {
            stateFlow.collectLatest { onResult?.invoke(it) }
        }
    }

    fun <T> mapperFlow(
        api: suspend () -> Response<ResponsePokeAPI<T>>,
        onResult: ((ResponsePokeAPI<T>?) -> Unit)? = null
    ) {
        //Get result form Data Source
        viewModelScope.launch {
            flow {
                val resultApi = api.invoke()
                if (resultApi.isSuccessful && resultApi.body() != null) {
                    emit(resultApi.body())
                } else {
                    emit(null)
                }
            }.flowOn(Dispatchers.IO).onStart {
                loadingState.postValue(LoadingState<T>(isLoading = true, data = null))
            }.catch {
                logDebug(it.message)
            }.flowOn(Dispatchers.IO).onCompletion {
                loadingState.postValue(LoadingState(isLoading = false, data = it))
            }.onEach {
                onResult?.invoke(it)
            }
        }
    }

    fun <T> mapperFlowSimple(
        api: suspend () -> Response<T>,
        onResult: ((T?) -> Unit)? = null
    ) {
        //Get result form Data Source
        viewModelScope.launch {
            flow {
                val resultApi = api.invoke()
                logDebug("mapperFlowSimple: ${resultApi.raw()}")
                if (resultApi.isSuccessful && resultApi.body() != null) {
                    emit(resultApi.body())
                } else {
                    emit(null)
                }
            }.onStart {
                loadingState.postValue(LoadingState<T>(isLoading = true, data = null))
            }.catch {
                logError("mapperFlowSimple: ${it.message}")
            }.flowOn(Dispatchers.IO).onCompletion {
                loadingState.postValue(LoadingState(isLoading = false, data = it))
            }.collectLatest {
                onResult?.invoke(it)
            }
        }
    }

    fun <T> mapperAsyncFlows(
        vararg apis: Pair<String, suspend () -> Response<ResponsePokeAPI<T>>>,
        tag: String = "",
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
                        }.catch {
                            send(Pair(api.first, null))
                            logError("API flow: ${api.first}: ${it.message}")
                        }.flowOn(Dispatchers.IO).collectLatest {
                            send(it)
                        }
                    }
                }
            }.onStart {
                loadingState.postValue(LoadingState<T>(isLoading = true, data = null, tag = tag))
            }.catch {
                logError(it.message)
            }.flowOn(Dispatchers.IO).onCompletion {
                onResult?.invoke(mutableMapAPI)
                loadingState.postValue(LoadingState(isLoading = false, data = it, tag = tag))
            }.collectLatest {
                mutableMapAPI[it.first] = it.second
            }
        }
    }

    fun <T> mapperSyncFlows(
        vararg apis: Pair<String, suspend () -> Response<ResponsePokeAPI<T>>>,
        tag: String = "",
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
                loadingState.postValue(LoadingState<T>(isLoading = true, data = null, tag = tag))
            }.catch {
                logDebug(it.message)
            }.flowOn(Dispatchers.IO).onCompletion {
                loadingState.postValue(LoadingState<T>(isLoading = false, data = null, tag = tag))
            }.collectLatest {
                onResult?.invoke(it)
            }
        }
    }
}