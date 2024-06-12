package com.huyhieu.data.retrofit

import com.huyhieu.data.BuildConfig
import com.huyhieu.domain.enitities.pokemon.PokemonUrlForm
import com.huyhieu.domain.enitities.responses.ResponsePokeAPI
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface PokeAPIService {

    companion object {
        val url: String
            get() = BuildConfig.POKE_API_URL
    }

    @GET("pokemon")
    suspend fun getPokemons(@QueryMap params: MutableMap<String, Any>): Response<ResponsePokeAPI<MutableList<PokemonUrlForm>>>

    @GET("pokemonsss")
    suspend fun getPokemonFailed(@QueryMap params: MutableMap<String, Any>): Response<ResponsePokeAPI<MutableList<PokemonUrlForm>>>

    @GET("pokemon/{idPokemon}/")
    suspend fun getPokemonDetail(@Path("idPokemon") idPokemon: String): Response<ResponsePokeAPI<PokemonUrlForm>>
}