package me.jamilalrasyidis.introductiontosceneform

import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.ar.core.*
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment

import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference
import androidx.appcompat.app.AlertDialog
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ux.TransformableNode


class MainActivity : AppCompatActivity() {

    private lateinit var fragment: ArFragment
    private val pointer: PointerDrawable = PointerDrawable()
    private var isTracking: Boolean = false
    private var isHitting: Boolean = false
    private lateinit var modelLoader: ModelLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        modelLoader = ModelLoader(WeakReference(this))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        fragment = supportFragmentManager.findFragmentById(R.id.fragment_sceneform) as (ArFragment)
        fragment.arSceneView.scene.addOnUpdateListener {
            fragment.onUpdate(it)
            onUpdate()
        }

        initializedGallery()
    }

    fun addNodeToScene(anchor: Anchor, renderable: ModelRenderable) {
        val anchorNode = AnchorNode(anchor)
        val transformableNode = TransformableNode(fragment.transformationSystem)
        transformableNode.renderable = renderable
        transformableNode.setParent(anchorNode)
        fragment.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }

    fun onException(throwable: Throwable) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(throwable.message).setTitle("AR Error")
        val dialog: AlertDialog = builder.create()
        dialog.show()
        return
    }

    private fun initializedGallery() {
        val gallery: LinearLayout = findViewById(R.id.ll_gallery)
        val andy = ImageView(this)
        andy.setImageResource(R.drawable.droid_thumb)
        andy.contentDescription = "Andy"
        andy.setOnClickListener {
            addObject(Uri.parse("andy_dance.sfb"))
        }
        gallery.addView(andy)

        val cabin = ImageView(this)
        cabin.setImageResource(R.drawable.cabin_thumb)
        cabin.contentDescription = "Cabin"
        cabin.setOnClickListener {
            addObject(Uri.parse("Cabin.sfb"))
        }
        gallery.addView(cabin)

        val house = ImageView(this)
        house.setImageResource(R.drawable.house_thumb)
        house.contentDescription = "House"
        house.setOnClickListener {
            addObject(Uri.parse("House.sfb"))
        }
        gallery.addView(house)

        val igloo = ImageView(this)
        igloo.setImageResource(R.drawable.igloo_thumb)
        igloo.contentDescription = "Igloo"
        igloo.setOnClickListener {
            addObject(Uri.parse("igloo.sfb"))
        }
        gallery.addView(igloo)
    }

    private fun addObject(model: Uri?) {
        val frame: Frame? = fragment.arSceneView.arFrame
        val pt: Point = getScreenCenter()
        val hits: List<HitResult>

        if (frame != null) {
            hits = frame.hitTest(pt.x.toFloat(), pt.y.toFloat())
            for (hit: HitResult in hits) {
                val trackable: Trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    modelLoader.loadModel(hit.createAnchor(), model)
                    break
                }
            }
        }
    }

    class ModelLoader(private var owner: WeakReference<MainActivity>) {

        fun loadModel(anchor: Anchor, uri: Uri?) {
            if (owner.get() == null) {
                Log.d(TAG, "Activity is null, Cannot load model")
                return
            }
            ModelRenderable.builder()
                .setSource(owner.get(), uri)
                .build()
                .handle<Any> { modelRenderable, throwable ->
                    val activity = owner.get()
                    when {
                        activity == null -> return@handle null
                        throwable != null -> activity.onException(throwable)
                        else -> activity.addNodeToScene(anchor, modelRenderable)
                    }
                    return@handle null
                }
            return
        }

        companion object {
            private const val TAG: String = "ModelLoader"
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

    private fun updateTracking(): Boolean {
        val frame: Frame? = fragment.arSceneView.arFrame
        val wasTracking = isTracking
        isTracking = frame != null && frame.camera.trackingState == TrackingState.TRACKING
        return isTracking != wasTracking
    }

    private fun updateHitTest(): Boolean {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
