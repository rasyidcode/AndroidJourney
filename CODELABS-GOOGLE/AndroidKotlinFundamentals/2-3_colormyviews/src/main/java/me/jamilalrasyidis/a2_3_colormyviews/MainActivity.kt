package me.jamilalrasyidis.a2_3_colormyviews

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListeners()
    }

    private fun setListeners() {
        val boxOne: TextView = findViewById(R.id.tv_box_one)
        val boxTwo: TextView = findViewById(R.id.tv_box_two)
        val boxThree: TextView = findViewById(R.id.tv_box_three)
        val boxFour: TextView = findViewById(R.id.tv_box_four)
        val boxFive: TextView = findViewById(R.id.tv_box_five)
        val rootConstraintLayout: ConstraintLayout = findViewById(R.id.constraint_layout)
        val clickableViews: List<View> = listOf(boxOne, boxTwo, boxThree, boxFour, boxFive, rootConstraintLayout)

        for (item in clickableViews) {
            item.setOnClickListener {
                makeColored(it)
            }
        }
    }

    private fun makeColored(view: View) {
        when(view.id) {
            R.id.tv_box_one -> view.setBackgroundColor(Color.DKGRAY)
            R.id.tv_box_two -> view.setBackgroundColor(Color.GRAY)
            R.id.tv_box_three -> view.setBackgroundColor(Color.BLUE)
            R.id.tv_box_four -> view.setBackgroundColor(Color.MAGENTA)
            R.id.tv_box_five -> view.setBackgroundColor(Color.BLACK)
            else -> view.setBackgroundColor(Color.LTGRAY)
        }
    }
}
