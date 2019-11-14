package me.jamilalrasyidis.armusicplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment

    private lateinit var musicPlayerView: ViewRenderable

    private lateinit var mp: MediaPlayer

    private var totalTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment
        mp = MediaPlayer.create(this, R.raw.music)
        mp.isLooping = true
        mp.setVolume(.5f, .5f)
        totalTime = mp.duration

        ViewRenderable.builder()
            .setView(this, R.layout.music_player_view)
            .build()
            .thenAccept { viewRenderable ->
                musicPlayerView = viewRenderable

                val volumeBar: SeekBar = musicPlayerView.view.findViewById(R.id.volume_bar)
                val positionBar: SeekBar = musicPlayerView.view.findViewById(R.id.position_bar)
                val elapsedTime: TextView = musicPlayerView.view.findViewById(R.id.elapsed_time_label)
                val remainingTime: TextView = musicPlayerView.view.findViewById(R.id.remaining_time_label)
                val btnPlay: Button = musicPlayerView.view.findViewById(R.id.play_btn)

                val createTimeLabel: (Int) -> String = {
                    var timeLabel: String
                    val min = it / 1000 / 60
                    val sec = it / 1000 % 60
                    timeLabel = "$min:"
                    if (sec < 10)
                        timeLabel += "0"
                    timeLabel += sec
                    timeLabel
                }

                val handler = @SuppressLint("HandlerLeak")
                object : Handler() {
                    override fun handleMessage(msg: Message) {
                        val currentPosition = msg.what
                        positionBar.progress = currentPosition
                        val elTime = createTimeLabel(currentPosition)
                        elapsedTime.text = elTime
                        val remTime = createTimeLabel(totalTime - currentPosition)
                        remainingTime.text = remTime
                    }
                }

                volumeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        if (p2) {
                            val volumeNum = p1 / 100.0f
                            mp.setVolume(volumeNum, volumeNum)
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {}

                    override fun onStopTrackingTouch(p0: SeekBar?) {}

                })

                positionBar.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        if (p2) {
                            mp.seekTo(p1)
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {}

                    override fun onStopTrackingTouch(p0: SeekBar?) {}

                })

                btnPlay.setOnClickListener {
                    if (mp.isPlaying) {
                        mp.pause()
                        btnPlay.text = "Pause"
                    } else {
                        mp.start()
                        btnPlay.text = "Play"
                    }
                }

                Thread(Runnable {
                    val msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                }).start()
            }

        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val anchor: Anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            val musicPlayer = TransformableNode(arFragment.transformationSystem)
            musicPlayer.setParent(anchorNode)
            musicPlayer.renderable = musicPlayerView
            musicPlayer.select()
        }
    }
}
