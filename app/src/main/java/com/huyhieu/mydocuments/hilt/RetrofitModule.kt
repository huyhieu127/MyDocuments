package com.huyhieu.mydocuments.hilt

import androidx.multidex.BuildConfig
import com.google.gson.GsonBuilder
import com.huyhieu.mydocuments.repository.remote.RemoteDataSource
import com.huyhieu.mydocuments.repository.remote.retrofit.APIService
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

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(APIService.url)
            .addConverterFactory(GsonConverterFactory.create(builder.create()))
    }

    @Singleton
    @Provides
    fun provideAPIService(retrofit: Retrofit.Builder): APIService {
        return retrofit
            .build()
            .create(APIService::class.java)
    }
}