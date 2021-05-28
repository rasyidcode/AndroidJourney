package me.jamilalrasyidis.testthing.handler.post_office_simulator

import android.util.Log
import java.lang.Exception
import java.util.*

class Simulator(
    private val postOffice: PostOffice,
    private val callback: Client.ClientCallback
) : Runnable {

    private val random: Random by lazy { Random(System.currentTimeMillis()) }

    private val thread: Thread by lazy { Thread(this) }

    private var controller: Boolean = true

    private val randomMessage: () -> String = {
        when (random.nextInt(10)) {
            0 -> "Happy Christmas"
            1 -> "How are you buddy?"
            2 -> "I am so proud of you"
            3 -> "Its holiday hahaha"
            4 -> ":P"
            5 -> "Lol!"
            6 -> "Wow!"
            7 -> "Buffer Off!"
            8 -> "I Love you!"
            9 -> "Go to hell :>"
            else -> "Hmmmm"
        }
    }
    override fun run() {
        controller = true
        while(controller) {
//            Log.d(TAG, "${Client.sClientMap.size}")
            val client1: Int = random.nextInt(Client.sClientMap.size)
            var client2: Int = random.nextInt(Client.sClientMap.size)
            while(client1 == client2) {
                client2 = random.nextInt(Client.sClientMap.size)
            }

            try {
                postOffice.sendPost(Post(client1, client2, randomMessage()))
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            try {
                Thread.sleep(2000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createClients(num: Int) : Simulator {
        for (i in 0 until num) {
            try {
                postOffice.register(Client("BOT $i", callback))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return this
    }

    @Synchronized
    fun start() {
        if (!thread.isAlive) {
            thread.start()
        }
    }

    fun stop() {
        controller = false
        Client.disposeAll()
    }

    companion object {
        const val TAG = "Simulator"
    }

}