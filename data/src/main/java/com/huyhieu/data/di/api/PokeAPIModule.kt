package com.huyhieu.data.di.api

import com.google.gson.GsonBuilder
import com.huyhieu.data.di.PokeAPI
import com.huyhieu.data.mapper.BooleanTypeAdapter
import com.huyhieu.data.retrofit.PokeAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokeAPIModule {
    @Singleton
    @Provides
    @PokeAPI
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(PokeAPIService.url)
            .addConverterFactory(GsonConverterFactory.create(builder.create()))
    }

    @Singleton
    @Provides
    @PokeAPI
    fun provideAPIService(@PokeAPI retrofit: Retrofit.Builder): PokeAPIService {
        return retrofit
            .build()
            .create(PokeAPIService::class.java)
    }
}
