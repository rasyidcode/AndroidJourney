package com.rcd.bambang.simpledota2stat.data.api

import com.rcd.bambang.simpledota2stat.data.vo.PlayerProfile
import com.rcd.bambang.simpledota2stat.data.vo.PlayerWinLose
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenDotaInterface {

    // player profile => https://api.opendota.com/api/players/194885925
    // player win lose => https://api.opendota.com/api/players/194885925/wl

    @GET("players/{player_id}")
    fun getPlayerProfile(@Path("player_id") playerId: Int): Single<PlayerProfile>

    @GET("players/{player_id}/wl")
    fun getPlayerWinLose(@Path("player_id") playerId: Int): Single<PlayerWinLose>

}