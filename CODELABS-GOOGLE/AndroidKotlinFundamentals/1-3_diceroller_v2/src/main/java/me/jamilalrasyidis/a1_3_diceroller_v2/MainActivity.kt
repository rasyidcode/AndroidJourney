package me.jamilalrasyidis.a1_3_diceroller_v2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var diceImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        diceImage = findViewById(R.id.iv_dice_image)
        val btnRollDice: Button = findViewById(R.id.btn_roll_dice)
        btnRollDice.setOnClickListener { rollDice() }

        val btnCountUp: Button = findViewById(R.id.btn_count_up)
        btnCountUp.setOnClickListener { countUp() }
    }

    private fun rollDice() {
        val drawableResource = when (Random.nextInt(6) + 1) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        diceImage.setImageResource(drawableResource)
    }

    private fun countUp() {
        if (diceImage.drawable.bytesEqualTo(ContextCompat.getDrawable(applicationContext, R.drawable.empty_dice))) {
            diceImage.setImageResource(R.drawable.dice_1)
        }
        var currentDice: Int =
            when {
                diceImage.drawable.bytesEqualTo(ContextCompat.getDrawable(applicationContext, R.drawable.dice_1)) -> 1
                diceImage.drawable.bytesEqualTo(ContextCompat.getDrawable(applicationContext, R.drawable.dice_2)) -> 2
                diceImage.drawable.bytesEqualTo(ContextCompat.getDrawable(applicationContext, R.drawable.dice_3)) -> 3
                diceImage.drawable.bytesEqualTo(ContextCompat.getDrawable(applicationContext, R.drawable.dice_4)) -> 4
                diceImage.drawable.bytesEqualTo(ContextCompat.getDrawable(applicationContext, R.drawable.dice_5)) -> 5
                else -> 6
            }

        if (currentDice < 6) {
            currentDice++
            diceImage.setImageResource(when (currentDice) {
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            })
        }
    }
}
