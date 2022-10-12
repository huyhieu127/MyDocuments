package com.huyhieu.mydocuments.repository.remote.retrofit

import com.huyhieu.mydocuments.BuildConfig
import com.huyhieu.mydocuments.models.pokemon.PokemonUrlForm
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface APIService {

    companion object {
        val url: String
            get() = BuildConfig.SERVER_URL
    }

    @GET("pokemon")
    suspend fun getPokemons(@QueryMap params: MutableMap<String, Any>): Response<ResponsePokeAPI<MutableList<PokemonUrlForm>>>

    @GET("pokemon")
    suspend fun getPokemonsss(@QueryMap params: MutableMap<String, Any>): Response<ResponseData<MutableList<PokemonUrlForm>>>

    @GET("pokemon/{idPokemon}/")
    suspend fun getPokemonDetail(@Path("idPokemon") idPokemon: String): Response<ResponseData<MutableList<PokemonUrlForm>>>
}