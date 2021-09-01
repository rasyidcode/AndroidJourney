package com.rcd.bambang.simpledota2stat.ui.player_profile

import androidx.lifecycle.LiveData
import com.rcd.bambang.simpledota2stat.data.api.OpenDotaInterface
import com.rcd.bambang.simpledota2stat.data.repository.NetworkState
import com.rcd.bambang.simpledota2stat.data.repository.PlayerProfileDataSource
import com.rcd.bambang.simpledota2stat.data.repository.PlayerProfileWinLoseDataSource
import com.rcd.bambang.simpledota2stat.data.vo.PlayerProfile
import com.rcd.bambang.simpledota2stat.data.vo.PlayerWinLose
import io.reactivex.disposables.CompositeDisposable
import kotlin.time.Duration

class PlayerProfileRepository(private val apiService: OpenDotaInterface) {

    lateinit var playerProfileDataSource: PlayerProfileDataSource

    lateinit var playerProfileWinLoseDataSource: PlayerProfileWinLoseDataSource

    fun fetchPlayerProfile(
        compositeDisposable: CompositeDisposable,
        playerId: Int
    ): LiveData<PlayerProfile> {
        playerProfileDataSource = PlayerProfileDataSource(apiService, compositeDisposable)
        playerProfileDataSource.fetchPlayerProfile(playerId)

        return playerProfileDataSource.playerProfile
    }

    fun fetchPlayerProfileWinLose(
        compositeDisposable: CompositeDisposable,
        playerId: Int
    ): LiveData<PlayerWinLose> {
        playerProfileWinLoseDataSource = PlayerProfileWinLoseDataSource(apiService, compositeDisposable)
        playerProfileWinLoseDataSource.fetchPlayerProfileWinLose(playerId)

        return playerProfileWinLoseDataSource.playerProfileWinLose
    }

    fun fetchPlayerProfileNetworkState(): LiveData<NetworkState> {
        return playerProfileDataSource.networkState
    }

    fun fetchPlayerProfileWinLoseNetworkState(): LiveData<NetworkState> {
        return playerProfileWinLoseDataSource.networkState
    }
}