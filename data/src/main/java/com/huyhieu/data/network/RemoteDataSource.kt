package com.huyhieu.data.network

import retrofit2.HttpException
import retrofit2.Response

open class RemoteDataSource {

    enum class ErrorServerType(val status: Int) {
        OK(200),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        NOT_FOUND(404),
        TIME_OUT(999)
    }

    protected suspend fun <T> getResultAPICore(remoteAPI: suspend () -> Response<ResponseData<T>>): ResultAPI<ResponseData<T>> {
        try {
            if (NetworkUtils.isAvailable) {
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

                } else if (response.code() == ErrorServerType.UNAUTHORIZED.status) {
                    return ResultAPI.expire(null)
                }
                return error("${response.code()} ${response.message()}")
            } else return ResultAPI.network()
        } catch (e: Exception) {
            return if (e is HttpException && e.code() == ErrorServerType.UNAUTHORIZED.status) {
                ResultAPI.expire(null)
            } else {
                error("${e.message}")
            }
        }
    }

    protected suspend fun <T> getPokeAPI(remoteAPI: suspend () -> Response<T>): ResultPokeAPI<T> {
        try {
            if (NetworkUtils.isAvailable) {
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