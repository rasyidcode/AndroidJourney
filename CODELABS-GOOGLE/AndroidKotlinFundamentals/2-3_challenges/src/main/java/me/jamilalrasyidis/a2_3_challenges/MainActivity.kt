package me.jamilalrasyidis.a2_3_challenges

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    private lateinit var boxOne: TextView
    private lateinit var boxTwo: TextView
    private lateinit var boxThree: TextView
    private lateinit var boxFour: TextView
    private lateinit var boxFive: TextView
    private lateinit var btnRed: Button
    private lateinit var btnBlue: Button
    private lateinit var btnGreen: Button
    private lateinit var rootConstraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boxOne = findViewById(R.id.tv_box_one)
        boxTwo = findViewById(R.id.tv_box_two)
        boxThree = findViewById(R.id.tv_box_three)
        boxFour = findViewById(R.id.tv_box_four)
        boxFive = findViewById(R.id.tv_box_five)
        btnRed = findViewById(R.id.btn_red)
        btnBlue = findViewById(R.id.btn_blue)
        btnGreen = findViewById(R.id.btn_green)
        rootConstraintLayout = findViewById(R.id.constraint_layout)

        setListeners()
    }

    private fun setListeners() {
        val clickableViews: List<View> = listOf(boxOne, boxTwo, boxThree, boxFour, boxFive, rootConstraintLayout, btnRed, btnBlue, btnGreen)

        for (item in clickableViews) {
            item.setOnClickListener {
                makeColored(it)
            }
        }
    }

    private fun makeColored(view: View) {
        when(view.id) {
            R.id.tv_box_one -> view.setBackgroundResource(R.drawable.box_one)
            R.id.tv_box_two -> view.setBackgroundResource(R.drawable.box_two)
            R.id.tv_box_three -> view.setBackgroundResource(R.drawable.box_three)
            R.id.tv_box_four -> view.setBackgroundResource(R.drawable.box_four)
            R.id.tv_box_five -> view.setBackgroundResource(R.drawable.box_five)
            R.id.btn_red -> boxThree.setBackgroundResource(R.color.my_green)
            R.id.btn_blue -> boxFour.setBackgroundResource(R.color.my_red)
            R.id.btn_green -> boxFive.setBackgroundResource(R.color.my_yellow)
            else -> view.setBackgroundColor(Color.YELLOW)
        }
    }
}
