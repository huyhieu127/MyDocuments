package com.huyhieu.data.di.api

import com.google.gson.GsonBuilder
import com.huyhieu.data.di.GitHubAPI
import com.huyhieu.data.retrofit.GitHubAPIService
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
object GitHubAPIModule {
    @Singleton
    @Provides
    @GitHubAPI
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        val builder = GsonBuilder()
        //builder.registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(GitHubAPIService.url)
            .addConverterFactory(GsonConverterFactory.create(builder.create()))
    }

    @Singleton
    @Provides
    @GitHubAPI
    fun provideAPIService(@GitHubAPI retrofit: Retrofit.Builder): GitHubAPIService {
        return retrofit.build().create(GitHubAPIService::class.java)
    }
}
