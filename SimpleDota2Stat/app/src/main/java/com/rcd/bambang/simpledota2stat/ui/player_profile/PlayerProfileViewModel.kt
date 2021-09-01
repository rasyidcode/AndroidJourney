package com.rcd.bambang.simpledota2stat.ui.player_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rcd.bambang.simpledota2stat.data.repository.NetworkState
import com.rcd.bambang.simpledota2stat.data.vo.PlayerProfile
import com.rcd.bambang.simpledota2stat.data.vo.PlayerWinLose
import io.reactivex.disposables.CompositeDisposable

class PlayerProfileViewModel(
    private val playerProfileRepository: PlayerProfileRepository,
    playerId: Int
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val playerProfile: LiveData<PlayerProfile> by lazy {
        playerProfileRepository.fetchPlayerProfile(compositeDisposable, playerId)
    }

    val playerProfileWinLose: LiveData<PlayerWinLose> by lazy {
        playerProfileRepository.fetchPlayerProfileWinLose(compositeDisposable, playerId)
    }

    val playerProfileNetworkState: LiveData<NetworkState> by lazy {
        playerProfileRepository.fetchPlayerProfileNetworkState()
    }

    val playerProfileWinLoseNetworkState: LiveData<NetworkState> by lazy {
        playerProfileRepository.fetchPlayerProfileWinLoseNetworkState()
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}