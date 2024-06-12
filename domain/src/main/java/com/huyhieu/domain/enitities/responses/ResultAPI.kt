package com.huyhieu.domain.enitities.responses

class ResultAPI<out T>(
    val statusAPI: StatusAPI,
    val response: T?,
    val message: String = "",
    val error: String? = null
) {

    enum class StatusAPI {
        LOADING,
        COMPLETE,
        NETWORK,
        EXPIRE,
        ERROR,
        SUCCESS
    }


    companion object {
        fun <T> success(response: ResponseData<T>): ResultAPI<ResponseData<T>> {
            return ResultAPI(
                StatusAPI.SUCCESS,
                response,
                ""
            )
        }

        fun <T> error(
            message: String = "",
            error: String? = null,
            response: ResponseData<T>? = null
        ): ResultAPI<ResponseData<T>> {
            return ResultAPI(
                StatusAPI.ERROR,
                response,
                message,
                error
            )
        }

        fun <T> network(): ResultAPI<ResponseData<T>> {
            return ResultAPI(
                StatusAPI.NETWORK,
                null, ""
            )
        }

        fun <T> loading(): ResultAPI<ResponseData<T>> {
            return ResultAPI(
                StatusAPI.LOADING,
                null,
                ""
            )
        }

        fun <T> expire(response: ResponseData<T>? = null): ResultAPI<ResponseData<T>> {
            return ResultAPI(
                StatusAPI.EXPIRE,
                response,
                ""
            )
        }

        fun <T> complete(): ResultAPI<ResponseData<T>> {
            return ResultAPI(
                StatusAPI.COMPLETE,
                null,
                ""
            )
        }
    }
}