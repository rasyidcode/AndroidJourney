package com.rcd.bambang.simpledota2stat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.rcd.bambang.simpledota2stat.ui.player_profile.PlayerProfileActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@MainActivity, PlayerProfileActivity::class.java))
            finish()
        }, 3000)
    }
}