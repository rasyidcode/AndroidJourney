package me.jamilalrasyidis.a2_2_aboutme_v2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDone = findViewById<Button>(R.id.btn_done)
        btnDone.setOnClickListener {
            addNickname(it)
        }

        val tvNickname: TextView = findViewById(R.id.tv_nickname)
        tvNickname.setOnClickListener {
            updateNickname(it)
        }
    }

    private fun updateNickname(view: View) {
        val etNickname: EditText = findViewById(R.id.et_nickname)
        val doneButton: Button = findViewById(R.id.btn_done)
        etNickname.visibility = View.VISIBLE
        doneButton.visibility = View.VISIBLE
        view.visibility = View.GONE
    }

    private fun addNickname(view: View) {
        val etNickname: EditText = findViewById(R.id.et_nickname)
        val tvNickname: TextView = findViewById(R.id.tv_nickname)
        tvNickname.text = etNickname.text
        etNickname.visibility = View.GONE
        tvNickname.visibility = View.VISIBLE
        view.visibility = View.GONE

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
