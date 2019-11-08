package me.jamilalrasyidis.twopointfour_aboutmedatabinding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import me.jamilalrasyidis.twopointfour_aboutmedatabinding.databinding.ActivityMainAboutBinding

class MainAboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainAboutBinding
    private val myName: MyName = MyName(name = "Jamil Al Rasyid")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_about)

        binding.myName = myName

        binding.btnDone.setOnClickListener {
            addNickname(it)
        }

        binding.tvNickname.setOnClickListener {
            updateNickname(it)
        }
    }

    private fun updateNickname(view: View) {
        binding.etNickname.visibility = View.VISIBLE
        binding.btnDone.visibility = View.VISIBLE
        view.visibility = View.GONE
    }

    private fun addNickname(view: View) {
        binding.tvNickname.text = binding.etNickname.text
        binding.etNickname.visibility = View.GONE
        binding.tvNickname.visibility = View.VISIBLE
        view.visibility = View.GONE

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
