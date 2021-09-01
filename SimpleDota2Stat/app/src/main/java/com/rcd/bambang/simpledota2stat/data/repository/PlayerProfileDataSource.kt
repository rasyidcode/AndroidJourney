package com.rcd.bambang.simpledota2stat.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rcd.bambang.simpledota2stat.data.api.OpenDotaInterface
import com.rcd.bambang.simpledota2stat.data.vo.PlayerProfile
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class PlayerProfileDataSource(
    private val apiService: OpenDotaInterface,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _playerProfile = MutableLiveData<PlayerProfile>()
    val playerProfile: LiveData<PlayerProfile>
        get() = _playerProfile

    fun fetchPlayerProfile(playerId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getPlayerProfile(playerId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _playerProfile.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        Log.e(javaClass.simpleName, "" + it.message)
                        _networkState.postValue(NetworkState.ERROR)
                    })
            )
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "" + e.message)
        }
    }
}