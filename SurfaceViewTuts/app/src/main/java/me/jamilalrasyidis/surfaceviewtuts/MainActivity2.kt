package me.jamilalrasyidis.surfaceviewtuts

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {

    private var cam: Camera? = null
    private lateinit var camPreview: CameraPreview
    private val btnCapture by lazy { findViewById<Button>(R.id.btn_capture) }
    private var btnSwitch: Button? = null
    private var cameraPreview: LinearLayout? = null
    private var isCameraFront: Boolean = false

    private val frontCameraId by lazy { findFrontFacingCamera() }
    private val backCameraId by lazy { findBackFacingCamera() }

    private val cameraCallback by lazy { PictureCallbackImpl() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        cameraPreview = findViewById(R.id.c_preview)
        camPreview = CameraPreview(this)
        cameraPreview?.addView(camPreview)

        btnCapture?.setOnClickListener {
            cam?.takePicture(null, null, cameraCallback)
        }

        btnSwitch = findViewById(R.id.btn_switch)
        btnSwitch?.setOnClickListener {
            releaseCamera()
            isCameraFront = !isCameraFront
            openCamera()
        }

        cam?.startPreview()

    }

    override fun onResume() {
        super.onResume()
        openCamera()
    }

    override fun onPause() {
        super.onPause()
        releaseCamera()
    }

    private fun findFrontFacingCamera(): Int {
        var cameraId = -1
        val numberOfCameras = Camera.getNumberOfCameras()
        Log.d("MainActivity2", numberOfCameras.toString())
        for (i in 0 until numberOfCameras) {
            val info: Camera.CameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i
                break
            }
        }
        return cameraId
    }

    private fun findBackFacingCamera(): Int {
        var cameraId = -1
        val numberOfCameras = Camera.getNumberOfCameras()
        Log.d("MainActivity2", numberOfCameras.toString())
        for (i in 0 until numberOfCameras) {
            val info: Camera.CameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i
                break
            }
        }
        return cameraId
    }

    private fun openCamera() {
        if (isCameraFront) {
            if (frontCameraId != -1) {
                cam = Camera.open(frontCameraId)
                camPreview.refreshCamera(cam)
            }
        } else {
            if (backCameraId != -1) {
                cam = Camera.open(backCameraId)
                camPreview.refreshCamera(cam)
            }
        }
    }

    private fun releaseCamera() {
        if (cam != null) {
            cam?.stopPreview()
            cam?.setPreviewCallback(null)
            cam?.release()
            cam = null
        }
    }

    companion object {
        var bitmap: Bitmap? = null
    }

    private inner class PictureCallbackImpl : Camera.PictureCallback {
        override fun onPictureTaken(data: ByteArray?, camera: Camera?) {
            Log.d("Capture", "asdf")
            bitmap = BitmapFactory.decodeByteArray(data, 0, data?.size ?: 0)
            startActivity(Intent(this@MainActivity2, PictureActivity::class.java))
        }
    }

}