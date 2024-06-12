package com.huyhieu.data.di.api

import com.google.gson.GsonBuilder
import com.huyhieu.data.di.ReqResAPI
import com.huyhieu.data.mapper.BooleanTypeAdapter
import com.huyhieu.data.retrofit.ReqResAPIService
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
object ReqResAPIModule {
    @Singleton
    @Provides
    @ReqResAPI
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ReqResAPIService.url)
            .addConverterFactory(GsonConverterFactory.create(builder.create()))
    }

    @Singleton
    @Provides
    @ReqResAPI
    fun provideAPIService(@ReqResAPI retrofit: Retrofit.Builder): ReqResAPIService {
        return retrofit
            .build()
            .create(ReqResAPIService::class.java)
    }
}