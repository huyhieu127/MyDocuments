package com.huyhieu.mydocuments.ui.fragments.multipleapi

import androidx.lifecycle.MutableLiveData
import com.huyhieu.mydocuments.base.BaseVM
import com.huyhieu.mydocuments.models.pokemon.PokemonUrlForm
import com.huyhieu.mydocuments.others.APIKey
import com.huyhieu.mydocuments.repository.remote.retrofit.ResponsePokeAPI
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MultipleApiVM @Inject constructor() : BaseVM() {
    var offset = 0
    var resultAPI: MutableStateFlow<ResponsePokeAPI<MutableList<PokemonUrlForm>>?> = MutableStateFlow(null)
    val fetchPokemonDetailLD: MutableLiveData<MutableMap<String, ResponsePokeAPI<MutableList<PokemonUrlForm>>?>?> = MutableLiveData(null)

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
        mapperAsyncFlows(
            Pair(APIKey.API_1) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 1000),
                    Pair("offset", offset),
                )
                apiService.getPokemonFailed(params)
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
            }
        ) {
            fetchPokemonDetailLD.value = it
        }

        /*mapperFlow.mapperFlow(this, resultAPI){
            val params = mutableMapOf<String, Any>(
                Pair("limit", 1000),
                Pair("offset", offset),
            )
            apiService.getPokemons(params)
        }*/
    }
}
