package com.huyhieu.mydocuments.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huyhieu.library.utils.logDebug
import com.huyhieu.mydocuments.repository.FlowMapper
import com.huyhieu.mydocuments.repository.LiveDataMapper
import com.huyhieu.mydocuments.repository.remote.retrofit.APIService
import com.huyhieu.mydocuments.repository.remote.retrofit.ResponseData
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

    var loadingState: MutableLiveData<Boolean> = MutableLiveData(false)

    fun <T> launch(
        stateFlow: MutableStateFlow<ResultAPI<ResponseData<T>>?>,
        onResult: ((ResultAPI<ResponseData<T>>?) -> Unit)? = null
    ) {
        viewModelScope.launch {
            stateFlow.collectLatest { onResult?.invoke(it) }
        }
    }


    fun <T> mapperFlow(
        vararg api: Pair<String, suspend () -> Response<ResponseData<T>>>,
        onResult: ((resp: ResultAPI<ResponseData<T>>?) -> Unit)? = null
    ) {
        //Get result form Data Source
        val flowRequestApi: Flow<ResultAPI<ResponseData<T>>> = flow {
            val resultApi = api.first().second.invoke()
            if (resultApi.isSuccessful && resultApi.body() != null) {
                emit(ResultAPI.success(resultApi.body()!!))
            } else {
                emit(ResultAPI.error("API Wrong!", "API not working!"))
            }
        }.onStart {
            loadingState.postValue(true)
        }

        flowRequestApi.flowOn(Dispatchers.IO)
        //Get result
        viewModelScope.launch {
            flowRequestApi.collectLatest {
                onResult?.invoke(it)
                loadingState.postValue(false)
                logDebug("API - Finish!!!")
            }
        }
    }
}