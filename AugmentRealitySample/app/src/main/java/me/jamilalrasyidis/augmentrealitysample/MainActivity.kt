package me.jamilalrasyidis.augmentrealitysample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.graphics.Point
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import org.w3c.dom.Text
import java.lang.ref.WeakReference
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity() {

    private lateinit var fragment: ArFragment

    private lateinit var viewLoader: ViewLoader

    private val pointer: PointerDrawable = PointerDrawable()

    private var isTracking: Boolean = false

    private var isHitting: Boolean = false

    private var isClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewLoader = ViewLoader(WeakReference(this))

        fragment = supportFragmentManager.findFragmentById(R.id.fragment_sceneform) as (ArFragment)
        fragment.arSceneView.scene.addOnUpdateListener {
            fragment.onUpdate(it)
            onUpdate()
        }

        val btnLoadView:Button = findViewById(R.id.btn_load_view)
        btnLoadView.setOnClickListener {
            addView()
        }
    }

    private fun onUpdate() {
        val trackingChanged: Boolean = updateTracking()
        val contentView: View = findViewById(android.R.id.content)
        if (trackingChanged) {
            if (isTracking) {
                contentView.overlay.add(pointer)
            } else {
                contentView.overlay.remove(pointer)
            }
            contentView.invalidate()
        }

        if (isTracking) {
            val hitTestChanged = updateHitTest()
            if (hitTestChanged) {
                pointer.enabled = isHitting
                contentView.invalidate()
            }
        }
    }

    private fun updateTracking() : Boolean {
        val frame: Frame? = fragment.arSceneView.arFrame
        val wasTracking = isTracking
        isTracking = frame != null && frame.camera.trackingState == TrackingState.TRACKING

        return isTracking != wasTracking
    }

    private fun updateHitTest() : Boolean {
        val frame:Frame? = fragment.arSceneView.arFrame
        val pt: Point = getScreenCenter()
        val hits: List<HitResult>
        val wasHitting = isHitting
        isHitting = false
        if (frame != null) {
            hits = frame.hitTest(pt.x.toFloat(), pt.y.toFloat())
            for (hit: HitResult in hits) {
                val trackable: Trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    isHitting = false
                    break
                }
            }
        }
        return wasHitting != isHitting
    }

    private fun getScreenCenter() : Point {
        val view: View = findViewById(android.R.id.content)
        return Point(view.width / 2, view.height / 2)
    }

    fun addNodeToScene(anchor: Anchor, renderable: ViewRenderable) {
        val anchorNode = AnchorNode(anchor)
        val transformableNode = TransformableNode(fragment.transformationSystem)
        transformableNode.renderable = renderable
        transformableNode.setParent(anchorNode)
        fragment.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }

    private fun addView() {
        val frame: Frame? = fragment.arSceneView.arFrame
        val pt: Point = getScreenCenter()
        val hits: List<HitResult>

        if (frame != null) {
            hits = frame.hitTest(pt.x.toFloat(), pt.y.toFloat())
            for(hit: HitResult in hits) {
                val trackable: Trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    viewLoader.loadView(hit.createAnchor())
                    break
                }
            }
        }
    }

    inner class ViewLoader(private var owner: WeakReference<MainActivity>) {

        fun loadView(anchor: Anchor) {
            if (owner.get() == null) {
                Log.d(TAG,"Activity is null, Cannot load model")
                return
            }
            ViewRenderable.builder()
                .setView(owner.get(), R.layout.textview_renderable)
                .build()
                .thenAccept {
                    val activity = owner.get()
                    val textView: TextView = it.view as TextView
                    textView.setOnClickListener {
                        if (isClicked) {
                            textView.setBackgroundResource(R.drawable.rounded_bg_blue)
                            textView.text = "Tap Here"
                            isClicked = false
                        } else {
                            textView.setBackgroundResource(R.drawable.rounded_bg_red)
                            textView.text = "You did it!"
                            isClicked = true
                        }
                    }
                    activity?.addNodeToScene(anchor, it)
                }

            return
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
