package com.rcd.bambang.daggerdota2stat

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    @SerializedName("account_id")
    val id: Long,
    @SerializedName("personaname")
    val name: String,
    @SerializedName("avatarfull")
    val avatarUrl: String,
) : Parcelable