package com.huyhieu.mydocuments.repository.remote.retrofit


data class ResultPokeAPI<out T>(
    val apiKey: String = "",
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
        fun <T> success(apiKey: String = "", response: T): ResultPokeAPI<T> {
            return ResultPokeAPI(
                apiKey = apiKey,
                StatusPokeAPI.SUCCESS,
                response,
                ""
            )
        }

        fun <T> error(
            apiKey: String = "",
            message: String = "",
            error: String? = null,
            response: T? = null
        ): ResultPokeAPI<T> {
            return ResultPokeAPI(
                apiKey = apiKey,
                StatusPokeAPI.ERROR,
                response,
                message,
                error
            )
        }

        fun <T> network(
            apiKey: String = ""
        ): ResultPokeAPI<T> {
            return ResultPokeAPI(
                apiKey = apiKey,
                StatusPokeAPI.NETWORK,
                null, ""
            )
        }

        fun <T> loading(apiKey: String = ""): ResultPokeAPI<T> {
            return ResultPokeAPI(
                apiKey = apiKey,
                StatusPokeAPI.LOADING,
                null,
                ""
            )
        }

        fun <T> complete(apiKey: String = ""): ResultPokeAPI<T> {
            return ResultPokeAPI(
                apiKey = apiKey,
                StatusPokeAPI.COMPLETE,
                null,
                ""
            )
        }
    }
}