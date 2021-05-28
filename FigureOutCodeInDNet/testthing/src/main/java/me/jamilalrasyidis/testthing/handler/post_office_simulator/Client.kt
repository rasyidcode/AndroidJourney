package me.jamilalrasyidis.testthing.handler.post_office_simulator

class Client(val name: String, private val callback: ClientCallback) {

    var id: Int

    init {
        id = sCounter++
        sClientMap[id] = this
    }

    @Synchronized
    fun onPostReceived(post: Post) {
        callback.onNewPost(
            sClientMap[post.senderId]!!,
            sClientMap[post.receiverId]!!,
            post.message!!
        )
    }

    @Synchronized
    fun dispose() {
        sClientMap.remove(id)
    }

    companion object {
        val sClientMap: LinkedHashMap<Int, Client> = LinkedHashMap()
        private var sCounter: Int = 0
        fun disposeAll() {
            sClientMap.clear()
        }
    }

    interface ClientCallback {
        fun onNewPost(receiver: Client, sender: Client, message: String)
    }
}