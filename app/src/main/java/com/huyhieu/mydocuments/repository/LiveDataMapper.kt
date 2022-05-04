package com.huyhieu.mydocuments.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.huyhieu.mydocuments.repository.remote.retrofit.ResponseData
import com.huyhieu.mydocuments.repository.remote.retrofit.ResultAPI
import com.huyhieu.mydocuments.repository.remote.retrofit.ResultPokeAPI
import com.huyhieu.mydocuments.utils.logDebug
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject

class LiveDataMapper @Inject constructor() : Repository() {

    fun <T> mapToLiveDataPokeAPI(vararg remoteAPI: suspend () -> Response<T>):
            LiveData<ResultPokeAPI<T>> = liveData(Dispatchers.IO) {
        //state loading
        val startTime = System.currentTimeMillis()
        emit(ResultPokeAPI.loading())

        //Get result form Data Source
        coroutineScope {
            //Call api
            launch(Dispatchers.IO) {
                remoteAPI.forEachIndexed { index, api ->
                    val callAPI = async {
                        val networkCall: suspend () -> ResultPokeAPI<T> = { getDataPokeAPI { api() } }
                        val resultAPI = networkCall.invoke()
                        when (resultAPI.statusPokeAPI) {
                            ResultPokeAPI.StatusPokeAPI.NETWORK -> {
                                emit(ResultPokeAPI.network<T>(index))
                            }

                            ResultPokeAPI.StatusPokeAPI.SUCCESS -> {
                                emit(ResultPokeAPI.success(index, resultAPI.response!!))
                            }

                            ResultPokeAPI.StatusPokeAPI.ERROR -> {
                                emit(ResultPokeAPI.error(index, resultAPI.message, resultAPI.error))
                            }
                            else -> {

                            }
                        }
                        logDebug("Time<$index>: ${System.currentTimeMillis() - startTime}")
                    }
                    callAPI.start()
                }
            }
        }
        emit(ResultPokeAPI.complete<T>())
        logDebug("Total time: ${System.currentTimeMillis() - startTime}")
    }

    fun <T> mapToLiveDataFromResult(remoteAPI: suspend () -> Response<ResponseData<T>>):
            LiveData<ResultAPI<ResponseData<T>>> = liveData(Dispatchers.IO) {
        //Get result form Data Source
        val networkCall: suspend () -> ResultAPI<ResponseData<T>> =
            { getDataFromRemote { remoteAPI() } }

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

    fun <T, A> mapToLiveDataFromResult(
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


