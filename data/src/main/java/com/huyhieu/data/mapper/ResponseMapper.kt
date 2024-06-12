package com.huyhieu.data.mapper

import com.huyhieu.data.network.NetworkStatus
import com.huyhieu.data.network.NetworkUtils
import com.huyhieu.domain.enitities.responses.ResponseData
import com.huyhieu.domain.enitities.responses.ResultAPI
import com.huyhieu.domain.enitities.responses.ResultPokeAPI
import retrofit2.HttpException
import retrofit2.Response

open class ResponseMapper {


    protected suspend fun <T> callAPI(remoteAPI: suspend () -> Response<ResponseData<T>>): ResultAPI<ResponseData<T>> {
        try {
            if (NetworkUtils.isNetworkAvailable) {
                val response = remoteAPI()
                if (response.isSuccessful) {
                    val body = response.body()

                    return if (body != null) {
                        //-1 expire
                        //o error
                        //1 success
                        when (body.code) {
                            -1 -> {
                                ResultAPI.expire()
                            }

                            1 -> {
                                ResultAPI.success(body)
                            }

                            else -> {
                                ResultAPI.error(body.message, body.error)
                            }
                        }
                    } else error("ResponseData empty")

                } else if (response.code() == NetworkStatus.UNAUTHORIZED.status) {
                    return ResultAPI.expire(null)
                }
                return error("${response.code()} ${response.message()}")
            } else return ResultAPI.network()
        } catch (e: Exception) {
            return if (e is HttpException && e.code() == NetworkStatus.UNAUTHORIZED.status) {
                ResultAPI.expire(null)
            } else {
                error("${e.message}")
            }
        }
    }

    protected suspend fun <T> callPokeAPI(remoteAPI: suspend () -> Response<T>): ResultPokeAPI<T> {
        try {
            if (NetworkUtils.isNetworkAvailable) {
                val response = remoteAPI()
                if (response.isSuccessful) {
                    val body = response.body()
                    return if (body != null) {
                        ResultPokeAPI.success(response = body)
                    } else {
                        ResultPokeAPI.error(message = "Empty data!")
                    }
                }
                return ResultPokeAPI.error(message = "${response.code()} ${response.message()}")
            } else return ResultPokeAPI.network()
        } catch (e: Exception) {
            return ResultPokeAPI.error(message = "${e.message}")
        }
    }


    private fun <T> error(message: String): ResultAPI<ResponseData<T>> {
        return ResultAPI.error("Network call has failed for a following reason: $message", null)
    }
}