package com.rcd.bambang.simpledota2stat.extensions

import com.rcd.bambang.simpledota2stat.data.api.OpenDotaClient
import com.rcd.bambang.simpledota2stat.data.repository.RankTier
import com.rcd.bambang.simpledota2stat.data.repository.RankTierStar

fun Int.toRankTierURLAsset(): String {
    val url = OpenDotaClient.RANK_MEDAL_BASE_URL
    return when (this) {
        RankTier.HERALD.id -> url + RankTier.HERALD.assetName
        RankTier.GUARDIAN.id -> url + RankTier.GUARDIAN.assetName
        RankTier.CRUSADER.id -> url + RankTier.CRUSADER.assetName
        RankTier.ARCHON.id -> url + RankTier.ARCHON.assetName
        RankTier.LEGEND.id -> url + RankTier.LEGEND.assetName
        RankTier.ANCIENT.id -> url + RankTier.ANCIENT.assetName
        RankTier.DIVINE.id -> url + RankTier.DIVINE.assetName
        RankTier.IMMORTAL.id -> url + RankTier.IMMORTAL.assetName
        else -> url + RankTier.UNCALIBRATED.assetName
    }
}

fun Int.toRankTierStarURLAsset(): String {
    val url = OpenDotaClient.RANK_MEDAL_BASE_URL
    return when (this) {
        RankTierStar.STAR_1.id -> url + RankTierStar.STAR_1.assetName
        RankTierStar.STAR_2.id -> url + RankTierStar.STAR_2.assetName
        RankTierStar.STAR_3.id -> url + RankTierStar.STAR_3.assetName
        RankTierStar.STAR_4.id -> url + RankTierStar.STAR_4.assetName
        RankTierStar.STAR_5.id -> url + RankTierStar.STAR_5.assetName
        else -> ""
    }
}