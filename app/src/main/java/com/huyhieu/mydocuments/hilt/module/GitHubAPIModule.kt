package com.huyhieu.mydocuments.hilt.module

import com.google.gson.GsonBuilder
import com.huyhieu.mydocuments.hilt.BooleanTypeAdapter
import com.huyhieu.mydocuments.repository.remote.retrofit.GitHubAPIService
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
object GitHubAPIModule {
    @Singleton
    @Provides
    @Named("GitHubAPI")
    fun provideGitHubAPI(okHttpClient: OkHttpClient): Retrofit.Builder {
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
    fun provideGitHubAPIService(@Named("GitHubAPI") retrofit: Retrofit.Builder): GitHubAPIService {
        return retrofit.build().create(GitHubAPIService::class.java)
    }
}
