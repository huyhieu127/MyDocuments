package com.huyhieu.data.di.api

import com.google.gson.GsonBuilder
import com.huyhieu.data.di.MapAPI
import com.huyhieu.data.retrofit.MapAPIService
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
object MapAPIModule {
    @Singleton
    @Provides
    @MapAPI
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        val builder = GsonBuilder()
        //builder.registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(MapAPIService.url)
            .addConverterFactory(GsonConverterFactory.create(builder.create()))
    }

    @Singleton
    @Provides
    @MapAPI
    fun provideAPIService(@MapAPI retrofit: Retrofit.Builder): MapAPIService {
        return retrofit.build().create(MapAPIService::class.java)
    }
}
