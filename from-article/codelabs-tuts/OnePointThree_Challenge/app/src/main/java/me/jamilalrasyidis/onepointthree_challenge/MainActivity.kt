package me.jamilalrasyidis.onepointthree_challenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var diceImageLeft: ImageView
    private lateinit var diceImageRight: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRollDice: Button = findViewById(R.id.btn_roll_dice)
        btnRollDice.setOnClickListener { rollDice() }

        val btnClearDice: Button = findViewById(R.id.btn_clear_dice)
        btnClearDice.setOnClickListener { clearDice() }

        diceImageLeft = findViewById(R.id.iv_dice_image_left)
        diceImageRight = findViewById(R.id.iv_dice_image_right)
    }

    private fun rollDice() {
        diceImageLeft.setImageResource(getRandomDiceImage())
        diceImageRight.setImageResource(getRandomDiceImage())
    }

    private fun clearDice() {
        diceImageLeft.setImageResource(R.drawable.empty_dice)
        diceImageRight.setImageResource(R.drawable.empty_dice)
    }

    private fun getRandomDiceImage(): Int {
        return when (Random.nextInt(6) + 1) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
    }
}
