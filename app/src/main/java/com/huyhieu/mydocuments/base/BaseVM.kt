package com.huyhieu.mydocuments.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huyhieu.data.di.GitHubAPI
import com.huyhieu.data.di.MapAPI
import com.huyhieu.data.di.PokeAPI
import com.huyhieu.data.di.ReqResAPI
import com.huyhieu.data.mapper.FlowMapper
import com.huyhieu.data.mapper.LiveDataMapper
import com.huyhieu.data.retrofit.GitHubAPIService
import com.huyhieu.data.retrofit.MapAPIService
import com.huyhieu.data.retrofit.PokeAPIService
import com.huyhieu.data.retrofit.ReqResAPIService
import com.huyhieu.domain.enitities.responses.ResponseData
import com.huyhieu.domain.enitities.responses.ResultAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

open class BaseVM : ViewModel() {

    var liveDataMapper = LiveDataMapper()

    var flowMapper = FlowMapper(this.viewModelScope)

    @Inject
    @MapAPI
    lateinit var mapAPIService: MapAPIService

    @Inject
    @GitHubAPI
    lateinit var gitHubAPIService: GitHubAPIService

    @Inject
    @PokeAPI
    lateinit var pokeApiService: PokeAPIService

    @Inject
    @ReqResAPI
    lateinit var reqResApiService: ReqResAPIService

    var loadingState: MutableLiveData<LoadingState<*>> = MutableLiveData()

    fun showLoading() {
        loadingState.postValue(LoadingState(isLoading = true, data = null))
    }

    fun hideLoading(throwable: Throwable? = null) {
        loadingState.postValue(LoadingState(isLoading = false, data = throwable))
    }

    fun <T> launch(
        stateFlow: MutableStateFlow<ResultAPI<ResponseData<T>>?>,
        onResult: ((ResultAPI<ResponseData<T>>?) -> Unit)? = null
    ) {
        viewModelScope.launch {
            stateFlow.collectLatest { onResult?.invoke(it) }
        }
    }

}