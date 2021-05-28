package me.jamilalrasyidis.testthing.handler.post_office_simulator

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import java.lang.Exception
import java.lang.ref.WeakReference


class PostOffice(private val clientDetailsMap: LinkedHashMap<Int?, Handler>) : HandlerThread(TAG) {

    @Synchronized
    @Throws(InvalidExceptionMessage::class, AlreadyExistException::class)
    fun register(client: Client?) {
        if (client == null) {
            throw InvalidExceptionMessage("The client can't be null")
        }
        Log.d(TAG, "${client.id}")
        if (clientDetailsMap.containsKey(client.id)) {
            throw AlreadyExistException("The client is already registered with this Id")
        }
        val clientWeakReference = WeakReference(client)
        val handler = object : Handler(looper) {
            override fun handleMessage(msg: Message) {
                val c = clientWeakReference.get()
                if (c != null) {
                    if (msg.obj is String) {
                        client.onPostReceived(Post(msg.arg1, msg.arg2, msg.obj as String))
                    } else {
                        client.onPostReceived(Post(msg.arg1, msg.arg2, "No Body present"))
                    }
                }
            }
        }
        clientDetailsMap[client.id] = handler
    }

    @Synchronized
    @Throws(InvalidExceptionMessage::class, NotRegisteredException::class)
    fun sendPost(post: Post?) {
        if (post == null) {
            throw InvalidExceptionMessage("Post can't be null")
        }
        if (post.receiverId == null || !clientDetailsMap.containsKey(post.receiverId!!)) {
            throw NotRegisteredException("Post receiver is not registered")
        }
        val handler = clientDetailsMap[post.receiverId!!]
        val message = Message()
        message.arg1 = post.senderId!!
        message.arg2 = post.receiverId!!
        message.obj = post.message
        handler?.sendMessage(message)
    }

    class InvalidExceptionMessage(message: String) : Exception(message)

    class AlreadyExistException(message: String) : Exception(message)

    class NotRegisteredException(message: String) : Exception(message)

    companion object {
        private const val TAG = "PostOffice"
    }
}