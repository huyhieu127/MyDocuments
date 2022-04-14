package com.huyhieu.mydocuments.repository.remote.retrofit


data class ResultPokeAPI<out T>(
    val statusPokeAPI: StatusPokeAPI,
    val response: T?,
    val message: String = "",
    val error: String? = null
) {

    enum class StatusPokeAPI {
        LOADING,
        COMPLETE,
        NETWORK,
        ERROR,
        SUCCESS
    }


    companion object {
        fun <T> success(response: T): ResultPokeAPI<T> {
            return ResultPokeAPI(
                StatusPokeAPI.SUCCESS,
                response,
                ""
            )
        }

        fun <T> error(
            message: String = "",
            error: String? = null,
            response: T? = null
        ): ResultPokeAPI<T> {
            return ResultPokeAPI(
                StatusPokeAPI.ERROR,
                response,
                message,
                error
            )
        }

        fun <T> network(): ResultPokeAPI<T> {
            return ResultPokeAPI(
                StatusPokeAPI.NETWORK,
                null, ""
            )
        }

        fun <T> loading(): ResultPokeAPI<T> {
            return ResultPokeAPI(
                StatusPokeAPI.LOADING,
                null,
                ""
            )
        }
        fun <T> complete(): ResultPokeAPI<T> {
            return ResultPokeAPI(
                StatusPokeAPI.COMPLETE,
                null,
                ""
            )
        }
    }
}