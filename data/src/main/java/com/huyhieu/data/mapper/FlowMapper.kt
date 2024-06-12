package com.huyhieu.data.mapper

import com.huyhieu.data.logger.logDebug
import com.huyhieu.data.logger.logError
import com.huyhieu.data.network.NetworkUtils
import com.huyhieu.domain.enitities.responses.ResponsePokeAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Response

class FlowMapper(private val coroutineScope: CoroutineScope) : ResponseMapper() {
    fun <T> executeAPI(
        apiService: suspend () -> Response<T>,
        onLoading: (() -> Unit)? = null,
        onCompletion: ((throwable: Throwable?) -> Unit)? = null,
        onResult: ((T?) -> Unit)? = null
    ) {
        //Get result form Data Source
        coroutineScope.launch {
            flow {
                if (NetworkUtils.isNetworkAvailable) {
                    val resultApi = apiService.invoke()
                    if (resultApi.isSuccessful && resultApi.body() != null) {
                        emit(resultApi.body())
                    } else {
                        emit(null)
                    }
                } else {
                    //emit(null)
                }
            }.onStart {
                onLoading?.invoke()
            }.catch {
                logError("executeAPI: $it")
            }.flowOn(Dispatchers.IO).onCompletion {
                onCompletion?.invoke(it)
            }.collectLatest {
                onResult?.invoke(it)
            }
        }
    }

    fun <T> executePokeAPI(
        api: suspend () -> Response<ResponsePokeAPI<T>>,
        onLoading: (() -> Unit)? = null,
        onCompletion: ((throwable: Throwable?) -> Unit)? = null,
        onResult: ((ResponsePokeAPI<T>?) -> Unit)? = null
    ) {
        //Get result form Data Source
        coroutineScope.launch {
            flow {
                val resultApi = api.invoke()
                if (resultApi.isSuccessful && resultApi.body() != null) {
                    emit(resultApi.body())
                } else {
                    emit(null)
                }
            }.flowOn(Dispatchers.IO).onStart {
                onLoading?.invoke()
            }.catch {
                logDebug(it.message)
            }.flowOn(Dispatchers.IO).onCompletion {
                onCompletion?.invoke(it)
            }.collectLatest {
                onResult?.invoke(it)
            }
        }
    }

    fun <T> executeAsyncAPI(
        vararg apis: Pair<String, suspend () -> Response<ResponsePokeAPI<T>>>,
        onLoading: ((tag: String) -> Unit)? = null,
        onCompletion: ((tag: String, throwable: Throwable?) -> Unit)? = null,
        tag: String = "",
        onResult: ((resp: MutableMap<String, ResponsePokeAPI<T>?>) -> Unit)? = null
    ) {
        //Get result form Data Source
        coroutineScope.launch {
            val mutableMapAPI = mutableMapOf<String, ResponsePokeAPI<T>?>()
            channelFlow {
                apis.forEachIndexed { idx, api ->
                    launch {
                        flow {
                            try {
                                val resultApi = api.second.invoke()
                                if (resultApi.isSuccessful && resultApi.body() != null) {
                                    emit(Pair(api.first, resultApi.body()))
                                } else {
                                    emit(Pair(api.first, null))
                                }
                            } catch (ex: Exception) {
                                logError("API - ${api.first}: \"ERROR\"")
                                emit(Pair(api.first, null))
                            }
                        }/*.catch {
                            send(Pair(api.first, null))
                            logError("API - ${api.first}: ${it.message}")
                        }*/.flowOn(Dispatchers.IO).collectLatest {
                            send(it)
                        }
                    }
                }
            }.onStart {
                onLoading?.invoke(tag)
            }.catch {
                logError(it.message)
            }.flowOn(Dispatchers.IO).onCompletion {
                onResult?.invoke(mutableMapAPI)
                onCompletion?.invoke(tag, it)
            }.collectLatest {
                mutableMapAPI[it.first] = it.second
            }
        }
    }

    fun <T> executeSyncAPI(
        vararg apis: Pair<String, suspend () -> Response<ResponsePokeAPI<T>>>,
        tag: String = "",
        onLoading: ((tag: String) -> Unit)? = null,
        onCompletion: ((tag: String, throwable: Throwable?) -> Unit)? = null,
        onResult: ((resp: Pair<String, ResponsePokeAPI<T>?>) -> Unit)? = null
    ) {
        //Get result form Data Source
        coroutineScope.launch {
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
                onLoading?.invoke(tag)
            }.catch {
                logDebug(it.message)
            }.flowOn(Dispatchers.IO).onCompletion {
                onCompletion?.invoke(tag, it)
            }.collectLatest {
                onResult?.invoke(it)
            }
        }
    }

}


