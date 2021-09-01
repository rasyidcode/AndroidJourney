package com.rcd.bambang.simpledota2stat.data.repository

class NetworkState(val status: Status, val message: String) {

    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "Running")
        val ERROR: NetworkState = NetworkState(Status.FAILED, "Something went wrong")
    }

}