package com.huyhieu.mydocuments.ui.fragments.multipleapi

import com.huyhieu.mydocuments.base.BaseVM
import com.huyhieu.mydocuments.models.pokemon.PokemonUrlForm
import com.huyhieu.mydocuments.others.APIKey
import com.huyhieu.mydocuments.repository.FlowMapper
import com.huyhieu.mydocuments.repository.LiveDataMapper
import com.huyhieu.mydocuments.repository.remote.retrofit.APIService
import com.huyhieu.mydocuments.repository.remote.retrofit.ResponseData
import com.huyhieu.mydocuments.repository.remote.retrofit.ResultAPI
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

data class MultipleApiVM @Inject constructor(
    private val mapper: LiveDataMapper,
    private val mapperFlow: FlowMapper,
    private val apiService: APIService
) : BaseVM() {
    var offset = 0
    var resultAPI: MutableStateFlow<ResultAPI<ResponseData<MutableList<PokemonUrlForm>>>?> =
        MutableStateFlow(null)

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

    /**
     * Call API with Flow*/
    fun fetchPokemonDetail() {
        mapperFlow.mapperFlow(this, resultAPI) {
            val params = mutableMapOf<String, Any>(
                Pair("limit", 1000),
                Pair("offset", offset),
            )
            apiService.getPokemonsss(params)
        }
    }
}
