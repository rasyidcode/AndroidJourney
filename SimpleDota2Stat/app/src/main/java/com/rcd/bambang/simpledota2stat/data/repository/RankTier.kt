package com.rcd.bambang.simpledota2stat.data.repository

class RankTier(val id: Int, val assetName: String) {
    companion object {
        val UNCALIBRATED: RankTier = RankTier(0, "rank_icon_0.png")
        val HERALD: RankTier = RankTier(1, "rank_icon_1.png")
        val GUARDIAN: RankTier = RankTier(2, "rank_icon_2.png")
        val CRUSADER: RankTier = RankTier(3, "rank_icon_3.png")
        val ARCHON: RankTier = RankTier(4, "rank_icon_4.png")
        val LEGEND: RankTier = RankTier(5, "rank_icon_5.png")
        val ANCIENT: RankTier = RankTier(6, "rank_icon_6.png")
        val DIVINE: RankTier = RankTier(7, "rank_icon_7.png")
        val IMMORTAL: RankTier = RankTier(8, "rank_icon_8.png")
    }
}