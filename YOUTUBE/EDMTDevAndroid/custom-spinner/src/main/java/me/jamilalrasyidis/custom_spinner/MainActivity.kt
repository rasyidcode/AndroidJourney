package me.jamilalrasyidis.custom_spinner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var heroes: List<Hero> = listOf(
        Hero(name = "Anti Mage", desc = "Agility Hero"),
        Hero(name = "Axe", desc = "Strengh Hero"),
        Hero(name = "Invoker", desc = "Intelegence Hero"),
        Hero(name = "Tinker", desc = "Intelegance Hero"),
        Hero(name = "Lion", desc = "Intelegance Hero")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = SpinnerAdapter(heroes, this@MainActivity)
        sp_list_hero.adapter = adapter
    }
}
