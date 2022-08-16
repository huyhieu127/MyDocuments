package com.huyhieu.mydocuments.ui.fragments.multipleapi

import androidx.lifecycle.ViewModel
import com.huyhieu.mydocuments.others.APIKey
import com.huyhieu.mydocuments.repository.LiveDataMapper
import com.huyhieu.mydocuments.repository.remote.retrofit.APIService
import javax.inject.Inject

data class MultipleApiVM @Inject constructor(
    private val mapper: LiveDataMapper,
    private val apiService: APIService
) : ViewModel() {
    var offset = 0

    /**Call multiple API*/
    fun getPokemons() =
        mapper.mapToLiveDataPokeAPI(
            Pair(APIKey.API_1) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 1000),
                    Pair("offset", offset),
                )
                apiService.getPokemons(params)
            },
            Pair(APIKey.API_2) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 10),
                    Pair("offset", offset + 1),
                )
                apiService.getPokemons(params)
            },
            Pair(APIKey.API_3) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 500),
                    Pair("offset", offset + 2),
                )
                apiService.getPokemons(params)
            })
}
