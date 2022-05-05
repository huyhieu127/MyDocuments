package com.huyhieu.mydocuments.ui.activities.multipleapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.huyhieu.mydocuments.models.MenuForm
import com.huyhieu.mydocuments.models.pokemon.PokemonUrlForm
import com.huyhieu.mydocuments.others.enums.MenuType
import com.huyhieu.mydocuments.repository.LiveDataMapper
import com.huyhieu.mydocuments.repository.remote.retrofit.APIService
import com.huyhieu.mydocuments.repository.remote.retrofit.ResponsePokeAPI
import com.huyhieu.mydocuments.repository.remote.retrofit.ResultPokeAPI
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.pow

data class MultipleAPIViewModel @Inject constructor(
    private val mapper: LiveDataMapper,
    private val apiService: APIService
) : ViewModel() {
    var offset = 0

    /**Call multiple API*/
    fun getPokemons() =
        mapper.mapToLiveDataPokeAPI(
            {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 1000),
                    Pair("offset", offset),
                )
                apiService.getPokemons(params)
            },
            {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 10),
                    Pair("offset", offset + 1),
                )
                apiService.getPokemons(params)
            },
            {
                val params = mutableMapOf<String, Any>(
                    Pair("limit", 500),
                    Pair("offset", offset + 2),
                )
                apiService.getPokemons(params)
            })
}
