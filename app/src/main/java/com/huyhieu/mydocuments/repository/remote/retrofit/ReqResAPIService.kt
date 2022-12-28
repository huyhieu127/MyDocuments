package com.huyhieu.mydocuments.repository.remote.retrofit

import com.google.gson.JsonObject
import com.huyhieu.mydocuments.BuildConfig
import retrofit2.Response
import retrofit2.http.GET

interface ReqResAPIService {

    companion object {
        val url: String
            get() = BuildConfig.REQRES_URL
    }

    @GET("users/2")
    suspend fun getUser(): Response<JsonObject>

}