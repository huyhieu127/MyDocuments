package com.huyhieu.mydocuments.hilt

import androidx.multidex.BuildConfig
import com.google.gson.GsonBuilder
import com.huyhieu.mydocuments.repository.remote.RemoteDataSource
import com.huyhieu.mydocuments.repository.remote.retrofit.PokeAPIService
import com.huyhieu.mydocuments.repository.remote.retrofit.ReqResAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitRetrofitModule {

    @Singleton
    @Provides
    fun provideInterceptor() = Interceptor { chain ->
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        try {
            requestBuilder.url(
                chain.request().url.toString()
            )
            requestBuilder
                .addHeader("Content-Type", "application/json;charset=UTF-8")

            chain.proceed(requestBuilder.build())
        } catch (e: java.lang.Exception) {
            when (e) {
                is SocketTimeoutException -> {
                    //FirebaseUtils.logCrashlytics(Exception("TimeOut: ${chain.request().url}"))
                }
                is UnknownHostException -> {
                    //FirebaseUtils.logCrashlytics(Exception("UnknownHostException: ${chain.request().url}"))
                }
                else -> {
                    //FirebaseUtils.logCrashlytics(e)
                }
            }
            val response = chain.proceed(request)
            response.newBuilder().code(RemoteDataSource.ErrorServerType.TIME_OUT.status)
                .message(e.message.toString()).build()
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(160, TimeUnit.SECONDS)
            .readTimeout(160, TimeUnit.SECONDS)
            .writeTimeout(160, TimeUnit.SECONDS)
            .build()
    } else {
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(160, TimeUnit.SECONDS)
            .readTimeout(160, TimeUnit.SECONDS)
            .writeTimeout(160, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Config for PokeAPIService
     * */
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

    /**
     * Config for ReqResAPIService
     * */
    @Singleton
    @Provides
    @Named("ReqResAPI")
    fun provideRetrofitResAPI(okHttpClient: OkHttpClient): Retrofit.Builder {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ReqResAPIService.url)
            .addConverterFactory(GsonConverterFactory.create(builder.create()))
    }

    @Singleton
    @Provides
    @Named("ReqResAPI")
    fun provideResAPIService(@Named("ReqResAPI") retrofit: Retrofit.Builder): ReqResAPIService {
        return retrofit
            .build()
            .create(ReqResAPIService::class.java)
    }
}