package me.jamilalrasyidis.eatit

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val typeFace: Typeface = Typeface.createFromAsset(assets, "NanumGothic-Regular.ttf")
        txt_slogan_desc.typeface = typeFace

        btn_sign_up.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        }

        btn_sign_in.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }
}
