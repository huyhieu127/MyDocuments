package com.huyhieu.data.di.app

import com.huyhieu.data.BuildConfig
import com.huyhieu.data.network.NetworkStatus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideInterceptor() = Interceptor { chain: Interceptor.Chain ->
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
            response.newBuilder().code(NetworkStatus.TIME_OUT.status)
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
}
