package com.huyhieu.mydocuments.repository

import com.huyhieu.mydocuments.repository.remote.RemoteDataSource
import com.huyhieu.mydocuments.repository.remote.retrofit.ResponseData
import retrofit2.Response

open class Repository : RemoteDataSource() {

    protected suspend fun <T> getDataPokeAPI(remoteAPI: suspend () -> Response<ResponseData<T>>) =
        getPokeAPI { remoteAPI() }

    protected suspend fun <T> getDataFromRemote(remoteAPI: suspend () -> Response<ResponseData<T>>) =
        getResultAPICore { remoteAPI() }

    protected suspend fun <T> getDataFromLocal(localAPI: suspend () -> Response<ResponseData<T>>) =
        getResultAPICore { localAPI() }
}