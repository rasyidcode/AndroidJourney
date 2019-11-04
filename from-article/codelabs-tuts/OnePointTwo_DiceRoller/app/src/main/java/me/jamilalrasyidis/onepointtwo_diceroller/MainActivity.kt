package me.jamilalrasyidis.onepointtwo_diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRollDice: Button = findViewById(R.id.btn_roll_dice)
        btnRollDice.setOnClickListener { rollDice() }
    }

    private fun rollDice() {
        val randomInt = Random.nextInt(6) + 1
        val tvResult: TextView = findViewById(R.id.tv_result)
        tvResult.text = randomInt.toString()
    }
}
