package com.huyhieu.data.mapper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.huyhieu.data.logger.logDebug
import com.huyhieu.domain.enitities.responses.ResponseData
import com.huyhieu.domain.enitities.responses.ResultAPI
import com.huyhieu.domain.enitities.responses.ResultPokeAPI
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class LiveDataMapper @Inject constructor() : ResponseMapper() {

    fun <T> executePokeAPI(vararg apiService: Pair<String, suspend () -> Response<T>>):
            LiveData<ResultPokeAPI<T>> = liveData(Dispatchers.IO) {
        //state loading
        val startTime = System.currentTimeMillis()
        emit(ResultPokeAPI.loading())
        //Get result form Data Source
        coroutineScope {
            //Call api
            launch(Dispatchers.IO) {
                var isNoInternet = false
                apiService.forEach { api ->
                    val callAPI = async {
                        val networkCall: suspend () -> ResultPokeAPI<T> =
                            { callPokeAPI { api.second() } }

                        val resultAPI = networkCall.invoke()
                        when (resultAPI.statusPokeAPI) {
                            ResultPokeAPI.StatusPokeAPI.NETWORK -> {
                                /*if (!isNoInternet) {
                                    isNoInternet = true
                                }*/
                                emit(ResultPokeAPI.network(api.first))
                                throw CancellationException("No internet")
                            }

                            ResultPokeAPI.StatusPokeAPI.SUCCESS -> {
                                emit(ResultPokeAPI.success(api.first, resultAPI.response!!))
                                logDebug("API - Success [${api.first}]")
                            }

                            ResultPokeAPI.StatusPokeAPI.ERROR -> {
                                emit(
                                    ResultPokeAPI.error(
                                        api.first,
                                        resultAPI.message,
                                        resultAPI.error
                                    )
                                )
                            }
                            else -> {

                            }
                        }
                    }
                    callAPI.start()
                    logDebug("API - Calling [${api.first}]")
                }
            }
        }
        emit(ResultPokeAPI.complete())
        logDebug("API - Finish!!!")
    }

    fun <T> executeAPI(remoteAPI: suspend () -> Response<ResponseData<T>>):
            LiveData<ResultAPI<ResponseData<T>>> = liveData(Dispatchers.IO) {
        //Get result form Data Source
        val networkCall: suspend () -> ResultAPI<ResponseData<T>> =
            { callAPI { remoteAPI() } }

        //Init live data
        val data: MutableLiveData<Result<T>> = MutableLiveData()
        data.value
        //state loading
        emit(ResultAPI.loading())

        //Call api
        val result = networkCall.invoke()
        when (result.statusAPI) {
            ResultAPI.StatusAPI.NETWORK -> {
                emit(ResultAPI.network<T>())
            }

            ResultAPI.StatusAPI.SUCCESS -> {
                emit(ResultAPI.success(result.response!!))
            }

            ResultAPI.StatusAPI.ERROR -> {
                emit(ResultAPI.error<T>(result.message, result.error))
            }

            ResultAPI.StatusAPI.EXPIRE -> {
                emit(ResultAPI.expire<T>())
            }

            else -> {

            }
        }
        emit(ResultAPI.complete<T>())
    }

    fun <T, A> executeAPI(
        databaseQuery: () -> LiveData<ResponseData<T>>,
        networkCall: suspend () -> ResultAPI<A>,
        saveCallResult: suspend (A) -> Unit
    ): LiveData<ResultAPI<ResponseData<T>>> = liveData(Dispatchers.IO) {

        //state loading
        emit(ResultAPI.loading<T>())

        //Query database
        val source = databaseQuery.invoke().map { ResultAPI.success(it) }
        emitSource(source)

        //Call api
        val response = networkCall.invoke()
        when (response.statusAPI) {

            ResultAPI.StatusAPI.NETWORK -> {
                emit(ResultAPI.network<T>())
            }

            ResultAPI.StatusAPI.SUCCESS -> {
                saveCallResult(response.response!!)
            }

            ResultAPI.StatusAPI.ERROR -> {
                emit(ResultAPI.error<T>(response.message))

            }

            ResultAPI.StatusAPI.EXPIRE -> {
                emit(ResultAPI.expire<T>())

            }
            else -> {

            }
        }
        emit(ResultAPI.complete())

    }

}


