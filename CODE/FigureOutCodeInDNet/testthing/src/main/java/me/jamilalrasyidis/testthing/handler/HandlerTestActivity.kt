package me.jamilalrasyidis.testthing.handler

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.jamilalrasyidis.testthing.R
import java.lang.Thread.sleep

class HandlerTestActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private val startProgress: Button by lazy { findViewById<Button>(R.id.startProgress) }
    private val textView: TextView by lazy { findViewById<TextView>(R.id.text_view) }

    private val max: Int = 100

    private var threadHandler: Handler? = null
    private var thread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler_test)

        progressBar = findViewById(R.id.progress_bar)
        progressBar.max = max

        thread = Thread(Runnable {
            for (i in 0 until 100) {
                Log.d("HandlerTestActivity", "$i")
                progressBar.progress = i
                try {
                    sleep(1000)
                } catch (ie: InterruptedException) {
                    ie.printStackTrace()
                }
                val message = Message()
                message.what = UPDATE_COUNT
                message.arg1 = i
                threadHandler?.sendMessage(message)
            }
        })

        startProgress.setOnClickListener {
//            val currentProgress = progressBar.progress
            threadHandler?.sendEmptyMessage(START_PROGRESS)
        }
    }

    override fun onResume() {
        super.onResume()

        threadHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == START_PROGRESS) {
                    thread?.start()
                } else if (msg.what == UPDATE_COUNT) {
                    textView.text = "Count ${msg.arg1}"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val START_PROGRESS = 100
        private const val UPDATE_COUNT = 101
    }

}