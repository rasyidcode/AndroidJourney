package com.rcd.bambang.simpledota2stat.data.repository

class RankTierStar(val id: Int, val assetName: String) {
    companion object {
        val STAR_1: RankTierStar = RankTierStar(1, "rank_star_1.png")
        val STAR_2: RankTierStar = RankTierStar(2, "rank_star_2.png")
        val STAR_3: RankTierStar = RankTierStar(3, "rank_star_3.png")
        val STAR_4: RankTierStar = RankTierStar(4, "rank_star_4.png")
        val STAR_5: RankTierStar = RankTierStar(5, "rank_star_5.png")
    }
}