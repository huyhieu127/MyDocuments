package com.huyhieu.mydocuments.repository.remote.retrofit


data class ResultPokeAPI<out T>(
    val index: Int = 0,
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
        fun <T> success(index: Int = 0,response: T): ResultPokeAPI<T> {
            return ResultPokeAPI(
                index = index,
                StatusPokeAPI.SUCCESS,
                response,
                ""
            )
        }

        fun <T> error(
            index: Int = 0,
            message: String = "",
            error: String? = null,
            response: T? = null
        ): ResultPokeAPI<T> {
            return ResultPokeAPI(
                index = index,
                StatusPokeAPI.ERROR,
                response,
                message,
                error
            )
        }

        fun <T> network(
            index: Int = 0
        ): ResultPokeAPI<T> {
            return ResultPokeAPI(
                index = index,
                StatusPokeAPI.NETWORK,
                null, ""
            )
        }

        fun <T> loading(index: Int = 0): ResultPokeAPI<T> {
            return ResultPokeAPI(
                index = index,
                StatusPokeAPI.LOADING,
                null,
                ""
            )
        }
        fun <T> complete(index: Int = 0): ResultPokeAPI<T> {
            return ResultPokeAPI(
                index = index,
                StatusPokeAPI.COMPLETE,
                null,
                ""
            )
        }
    }
}