package com.huyhieu.mydocuments.hilt.module

import com.google.gson.GsonBuilder
import com.huyhieu.mydocuments.hilt.BooleanTypeAdapter
import com.huyhieu.mydocuments.repository.remote.retrofit.PokeAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokeAPIModule {
    @Singleton
    @Provides
    @Named("PokeAPI")
    fun provideRetrofitPokeAPI(okHttpClient: OkHttpClient): Retrofit.Builder {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(PokeAPIService.url)
            .addConverterFactory(GsonConverterFactory.create(builder.create()))
    }

    @Singleton
    @Provides
    @Named("PokeAPI")
    fun providePokeAPIService(@Named("PokeAPI") retrofit: Retrofit.Builder): PokeAPIService {
        return retrofit
            .build()
            .create(PokeAPIService::class.java)
    }
}
