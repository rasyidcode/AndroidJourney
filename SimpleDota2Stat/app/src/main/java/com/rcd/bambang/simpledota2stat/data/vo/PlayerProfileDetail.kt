package com.rcd.bambang.simpledota2stat.data.vo


import com.google.gson.annotations.SerializedName

data class PlayerProfileDetail(
    @SerializedName("account_id")
    val accountId: Int,
    val avatar: String,
    @SerializedName("avatarfull")
    val avatarFull: String,
    @SerializedName("avatarmedium")
    val avatarMedium: String,
    val cheese: Int,
    @SerializedName("is_contributor")
    val isContributor: Boolean,
    @SerializedName("last_login")
    val lastLogin: String,
    val loccountrycode: String,
    val name: Any,
    val personaname: String,
    val plus: Boolean,
    val profileurl: String,
    val steamid: String
)