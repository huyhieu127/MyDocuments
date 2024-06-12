package com.huyhieu.mydocuments.ui.fragments.network.multipleapi

import androidx.lifecycle.MutableLiveData
import com.huyhieu.domain.enitities.pokemon.PokemonUrlForm
import com.huyhieu.domain.enitities.responses.ResponsePokeAPI
import com.huyhieu.mydocuments.base.BaseVM
import com.huyhieu.mydocuments.others.APIKey
import javax.inject.Inject

class MultipleApiVM @Inject constructor() : BaseVM() {
    var offset = 0
    val fetchAsyncPokemonDetailLD: MutableLiveData<MutableMap<String, ResponsePokeAPI<MutableList<PokemonUrlForm>>?>?> = MutableLiveData(null)
    val fetchSyncPokemonDetailLD: MutableLiveData<Pair<String, ResponsePokeAPI<MutableList<PokemonUrlForm>>?>?> = MutableLiveData(null)

    /**Call multiple API*/
    fun getPokemons() =
        liveDataMapper.executePokeAPI(
            Pair(APIKey.API_1) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 1000),
                    Pair("offset", offset),
                )
                pokeApiService.getPokemons(params)
            },
            Pair(APIKey.API_2) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 10),
                    Pair("offset", offset + 1),
                )
                pokeApiService.getPokemons(params)
            },
            Pair(APIKey.API_3) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 500),
                    Pair("offset", offset + 2),
                )
                pokeApiService.getPokemons(params)
            })

    /**
     * Call API with Flow*/
    fun fetchAsyncPokemonDetail() {
        flowMapper.executeAsyncAPI(
            Pair(APIKey.API_1) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 1000),
                    Pair("offset", offset),
                )
                pokeApiService.getPokemons(params)
            },
            Pair(APIKey.API_2) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 10),
                    Pair("offset", offset + 1),
                )
                pokeApiService.getPokemons(params)
            },
            Pair(APIKey.API_3) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 500),
                    Pair("offset", offset + 2),
                )
                pokeApiService.getPokemons(params)
            },
            tag = "async",
            onLoading = {
                showLoading()
            },
            onCompletion = { _, _ ->
                hideLoading()
            },
        ) {
            fetchAsyncPokemonDetailLD.value = it
        }
    }

    fun fetchSyncPokemonDetail() {
        flowMapper.executeSyncAPI(
            Pair(APIKey.API_1) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 1000),
                    Pair("offset", offset),
                )
                pokeApiService.getPokemonFailed(params)
            },
            Pair(APIKey.API_2) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 10),
                    Pair("offset", offset + 1),
                )
                pokeApiService.getPokemons(params)
            },
            Pair(APIKey.API_3) {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 500),
                    Pair("offset", offset + 2),
                )
                pokeApiService.getPokemons(params)
            },
            tag = "sync",
            onLoading = {
                showLoading()
            },
            onCompletion = { _, _ ->
                hideLoading()
            },
        ) {
            fetchSyncPokemonDetailLD.value = it
        }
    }
}
