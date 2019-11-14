package me.jamilalrasyidis.a2_4_aboutme_databinding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import me.jamilalrasyidis.a2_4_aboutme_databinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val myName: MyName = MyName(name = "Jamil Al Rasyid")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            myname = myName
            btnDone.setOnClickListener {
                addNickname(it)
            }
            tvNickname.setOnClickListener {
                updateNickname(it)
            }
        }
    }

    private fun updateNickname(view: View) {
        binding.apply {
            etNickname.visibility = View.VISIBLE
            btnDone.visibility = View.VISIBLE
        }
        view.visibility = View.GONE
    }

    private fun addNickname(view: View) {
        binding.apply {
            myName.nickname = etNickname.text.toString()
            invalidateAll()
            etNickname.visibility = View.GONE
            tvNickname.visibility = View.VISIBLE
        }
        view.visibility = View.GONE

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
