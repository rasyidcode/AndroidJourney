package com.rcd.bambang.simpledota2stat.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rcd.bambang.simpledota2stat.data.api.OpenDotaInterface
import com.rcd.bambang.simpledota2stat.data.vo.PlayerProfile
import com.rcd.bambang.simpledota2stat.data.vo.PlayerWinLose
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PlayerProfileWinLoseDataSource(
    private val apiService: OpenDotaInterface,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _playerProfileWinLose = MutableLiveData<PlayerWinLose>()
    val playerProfileWinLose: LiveData<PlayerWinLose>
        get() = _playerProfileWinLose

    fun fetchPlayerProfileWinLose(playerId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getPlayerWinLose(playerId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _playerProfileWinLose.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        Log.e(javaClass.simpleName, ""+it.message)
                        _networkState.postValue(NetworkState.ERROR)
                    })
            )
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, ""+e.message)
            _networkState.postValue(NetworkState.ERROR)
        }
    }
}