package me.jamilalrasyidis.a1_2_challenges

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

        val btnCountUp: Button = findViewById(R.id.btn_count_up)
        btnCountUp.setOnClickListener { countUp() }
    }

    private fun rollDice() {
        val randomInt = Random.nextInt(6) + 1
        val tvResult: TextView = findViewById(R.id.tv_result)
        tvResult.text = randomInt.toString()
    }

    private fun countUp() {
        val tvResult: TextView = findViewById(R.id.tv_result)

        if (tvResult.text == resources.getString(R.string.text_hello_world)) {
            tvResult.text = 1.toString()
        }

        var resultInt = tvResult.text.toString().toInt()

        if (resultInt < 6) {
            resultInt++
            tvResult.text = resultInt.toString()
        }
    }

}
