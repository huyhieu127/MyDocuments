package com.huyhieu.mydocuments.hilt.module

import com.google.gson.GsonBuilder
import com.huyhieu.mydocuments.repository.remote.retrofit.GitHubAPIService
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
object GitHubAPIModule {
    @Singleton
    @Provides
    @Named("GitHubAPI")
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
    @Named("GitHubAPI")
    fun provideAPIService(@Named("GitHubAPI") retrofit: Retrofit.Builder): GitHubAPIService {
        return retrofit.build().create(GitHubAPIService::class.java)
    }
}
