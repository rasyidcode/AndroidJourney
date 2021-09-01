package com.rcd.bambang.simpledota2stat.data.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object OpenDotaClient {

    const val BASE_URL = "https://api.opendota.com/api/"
    const val RANK_MEDAL_BASE_URL = "https://www.opendota.com/assets/images/dota2/rank_icons/"

    fun getClient() : OpenDotaInterface {
        val requestInterceptor = Interceptor {
            val url: HttpUrl = it.request()
                .url()
                .newBuilder()
                .build()

            val request: Request = it.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenDotaInterface::class.java)
    }

}