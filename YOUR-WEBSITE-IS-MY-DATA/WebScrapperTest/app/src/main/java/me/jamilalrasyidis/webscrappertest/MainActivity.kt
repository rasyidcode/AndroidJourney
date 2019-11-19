package me.jamilalrasyidis.webscrappertest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread{
            val doc = Jsoup.connect("https://kprofiles.com/black-pink-members-profile/").get()
            Log.d("MainActivity", "title : "+doc.title().toString())
            Log.d("MainActivity", "data : "+doc.select("h1.entry-title").text().toString())
        }.start()

        Thread{
            val doc = Jsoup.connect("$KPOP_PROFILES_BASE_URL/k-pop-girl-groups/").get()
            val profiles = doc.select("div.entry-content.herald-entry-content")
            profiles.select("p").forEach {pTag ->
//                Log.d("MainActivity", "profile : "+it.text().toString())
//                Log.d("MainActivity", "profile : "+it.select("a").text().toString())
                pTag.select("a").forEach {
                    if (it.nextElementSibling() != null && it.nextElementSibling().hasText()) {
                        Log.d("MainActivity", it.text().toString() + " - " + it.nextElementSibling().text().toString())
                    } else {
                        Log.d("MainActivity", it.text().toString())
                    }
                }
                Log.d("MainActivity", "=========== - ===========")
            }
        }.start()

        Thread {
            Log.d("MainActivity", "HELLO SCRAPPER!")
        }.start()
    }

    companion object {
        const val KPOP_PROFILES_BASE_URL = "https://kprofiles.com/"
    }
}
