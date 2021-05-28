package me.jamilalrasyidis.surfaceviewtuts

import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseIntArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.Surface.ROTATION_270
import android.view.Surface.ROTATION_180
import android.view.Surface.ROTATION_90
import android.view.Surface.ROTATION_0
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var surfaceView: SurfaceView
    private var camera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ORIENTATION_MAP.put(ROTATION_0, 0)
        ORIENTATION_MAP.put(ROTATION_90, 90)
        ORIENTATION_MAP.put(ROTATION_180, 180)
        ORIENTATION_MAP.put(ROTATION_270, 270)

        surfaceView = findViewById(R.id.surface_view)
        initView()
    }

    override fun onResume() {
        super.onResume()

        obtainCamera()
    }

    override fun onPause() {
        super.onPause()

        releaseCamera()
    }

    private fun initView() {
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}

            override fun surfaceDestroyed(p0: SurfaceHolder?) {}

            override fun surfaceCreated(p0: SurfaceHolder?) {
                startCamera(p0)
            }
        })
    }

    private fun obtainCamera() {
        if (camera == null) {
            camera = Camera.open(0)
        }
    }

    private fun releaseCamera() {
        if (camera != null) {
            camera?.setPreviewCallback(null)
            camera?.stopPreview()
            camera?.release()

            camera = null
        }
    }

    private fun startCamera(holder: SurfaceHolder?) {
        val camParams: Camera.Parameters? = camera?.parameters
        val sizes: MutableList<Camera.Size>? = camParams?.supportedPreviewSizes
        val s: Camera.Size = sizes!![0]

        camParams.setPreviewSize(s.width, s.height)
        camParams.whiteBalance = Camera.Parameters.WHITE_BALANCE_AUTO
        if (camParams.supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            camParams.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        }

        camera?.parameters = camParams

        val info = Camera.CameraInfo()
        Camera.getCameraInfo(0, info)

        val orientation = windowManager.defaultDisplay.rotation
        val degrees = (info.orientation - ORIENTATION_MAP.get(orientation) + 360) % 360

        camera?.setDisplayOrientation(degrees)

        try {
            camera?.setPreviewDisplay(holder)
            camera?.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        val ORIENTATION_MAP: SparseIntArray = SparseIntArray()
    }
}
