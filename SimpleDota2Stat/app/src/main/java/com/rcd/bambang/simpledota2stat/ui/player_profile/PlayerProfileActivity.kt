package com.rcd.bambang.simpledota2stat.ui.player_profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rcd.bambang.simpledota2stat.R
import com.rcd.bambang.simpledota2stat.data.api.OpenDotaClient
import com.rcd.bambang.simpledota2stat.data.api.OpenDotaInterface
import com.rcd.bambang.simpledota2stat.data.repository.NetworkState
import com.rcd.bambang.simpledota2stat.data.vo.PlayerProfile
import com.rcd.bambang.simpledota2stat.data.vo.PlayerWinLose
import com.rcd.bambang.simpledota2stat.databinding.ActivityPlayerProfileBinding
import com.rcd.bambang.simpledota2stat.extensions.toRankTierStarURLAsset
import com.rcd.bambang.simpledota2stat.extensions.toRankTierURLAsset

class PlayerProfileActivity : AppCompatActivity() {

    private lateinit var playerProfileViewModel: PlayerProfileViewModel
    private lateinit var playerProfileRepository: PlayerProfileRepository

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityPlayerProfileBinding>(
            this@PlayerProfileActivity,
            R.layout.activity_player_profile
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val playerId = 194885925
        val apiService: OpenDotaInterface = OpenDotaClient.getClient()
        playerProfileRepository = PlayerProfileRepository(apiService)
        playerProfileViewModel = getPlayerProfileViewModel(playerId)
        playerProfileViewModel.playerProfile.observe(this@PlayerProfileActivity, {
            bindUIPlayerProfile(it)
        })

        playerProfileViewModel.playerProfileNetworkState.observe(this@PlayerProfileActivity, {
            binding.pbLoading.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
        })

        playerProfileViewModel.playerProfileWinLose.observe(this@PlayerProfileActivity, {
            bindUIPlayerProfileWinLose(it)
        })

        playerProfileViewModel.playerProfileWinLoseNetworkState.observe(
            this@PlayerProfileActivity,
            {
                binding.pbLoading.visibility =
                    if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            })
    }

    private fun bindUIPlayerProfileWinLose(playerWinLose: PlayerWinLose) {
        binding.tvWin.text = playerWinLose.win.toString()
        binding.tvLose.text = playerWinLose.lose.toString()
    }

    private fun bindUIPlayerProfile(playerProfile: PlayerProfile) {
        binding.tvPlayerName.text = playerProfile.playerProfileDetail.personaname
        Glide.with(this@PlayerProfileActivity)
            .load(playerProfile.playerProfileDetail.avatarFull)
            .into(binding.ivPlayerAvatar)

        val rankAndStar = playerProfile.rankTier.toString()

        val rank = rankAndStar[0].digitToInt()
        val star = rankAndStar[1].digitToInt()

        Glide.with(this@PlayerProfileActivity)
            .load(6.toRankTierURLAsset())
            .into(binding.ivRankMedal)

        Glide.with(this@PlayerProfileActivity)
            .load(5.toRankTierStarURLAsset())
            .into(binding.ivRankMedalStars)

        Log.d(javaClass.simpleName, "rank url : ${rank.toRankTierURLAsset()}")
        Log.d(javaClass.simpleName, "star url : ${star.toRankTierStarURLAsset()}")
    }

    private fun getPlayerProfileViewModel(playerId: Int): PlayerProfileViewModel {
        return ViewModelProvider(
            this@PlayerProfileActivity,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return PlayerProfileViewModel(playerProfileRepository, playerId) as T
                }
            })[PlayerProfileViewModel::class.java]

    }
}