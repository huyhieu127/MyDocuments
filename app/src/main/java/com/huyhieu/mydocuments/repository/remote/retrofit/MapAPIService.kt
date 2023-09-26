package com.huyhieu.mydocuments.repository.remote.retrofit

import com.google.gson.JsonObject
import com.huyhieu.mydocuments.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MapAPIService {
    companion object {
        val url: String
            get() = BuildConfig.MAP_API_URL
    }

    @GET("directions/{output}")
    suspend fun getDirections(
        @Path("output") output: String,
        @QueryMap parameters: MutableMap<String, Any>
    ): Response<JsonObject>
}