package me.jamilalrasyidis.surfaceviewtuts

import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException
import java.lang.Exception

// auto focus
//

class CameraPreview(
    context: Context
) : SurfaceView(context), SurfaceHolder.Callback {

    private var camera: Camera? = null

    private var sufHolder: SurfaceHolder? = null

    init {
        sufHolder = holder
        sufHolder?.addCallback(this)
        sufHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    fun refreshCamera(cam: Camera?) {
        if (sufHolder?.surface == null) {
            return
        }

        try {
            cam?.stopPreview()
        } catch (e: Exception) {
        }

        setCamera(camera)
        try {
            cam?.setPreviewDisplay(sufHolder)
            cam?.startPreview()
        } catch (e: Exception) {
            Log.d("CameraPreview", "Error starting camera preview: ${e.message}")
        }
    }

    private fun setCamera(camera: Camera?) {
        this.camera = camera
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: SurfaceHolder?) {}

    override fun surfaceCreated(p0: SurfaceHolder?) {
        Log.d("CameraPreview", "Surface Created")
        try {
            if (camera == null) {
                camera?.setPreviewDisplay(holder)
                camera?.startPreview()
            }
        } catch (e: IOException) {
            Log.d("CameraPreview", "Error setting camera preview: ${e.message}")
        }
    }

}