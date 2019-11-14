package me.jamilalrasyidis.resocodertuts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val progressText by lazy { findViewById<TextView>(R.id.tv_progress) }
    private val seekBarMain by lazy { findViewById<SeekBar>(R.id.sb_main) }
    private val btnReset by lazy { findViewById<Button>(R.id.btn_reset) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val initialTextViewTranslationY = progressText.translationY

        seekBarMain.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, p1: Int, p2: Boolean) {
                progressText.text = p1.toString()
                val translationDistance = (initialTextViewTranslationY + p1 * resources.getDimension(R.dimen.text_translation_distance) * -1)
                progressText.animate().translationY(translationDistance)
                if (!p2) {
                    progressText.animate().setDuration(500).rotationBy(360f).translationY(initialTextViewTranslationY)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        btnReset.setOnClickListener {
            seekBarMain.progress = 0
        }
    }
}
