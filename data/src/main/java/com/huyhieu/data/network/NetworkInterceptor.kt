package com.huyhieu.data.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        return try {
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
}