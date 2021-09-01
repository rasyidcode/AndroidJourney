package com.rcd.bambang.simpledota2stat.data.vo


import com.google.gson.annotations.SerializedName

data class PlayerProfile(
    @SerializedName("leaderboard_rank")
    val leaderboardRank: Int?,
    @SerializedName("profile")
    val playerProfileDetail: PlayerProfileDetail,
    @SerializedName("rank_tier")
    val rankTier: Int,
)