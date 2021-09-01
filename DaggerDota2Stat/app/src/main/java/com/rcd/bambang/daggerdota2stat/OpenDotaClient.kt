package com.rcd.bambang.daggerdota2stat

import retrofit2.Call
import retrofit2.http.GET

interface OpenDotaClient {

    @GET("search")
    fun searchPlayers(name: String): Call<List<Player>>

    companion object {
        const val BASE_URL = "https://api.opendota.com/api/"
    }
}