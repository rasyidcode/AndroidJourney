package me.jamilalrasyidis.surfaceviewtuts

import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream
import java.lang.Exception
import java.lang.RuntimeException
import android.R.attr.data
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.io.FileNotFoundException
import java.io.IOException


class SurfaceViewActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var btnCapture: Button
    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var camera: Camera
    private lateinit var rawCallback: Camera.PictureCallback
    private lateinit var shutterCallback: Camera.ShutterCallback
    private lateinit var jpegCallback: Camera.PictureCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.btn_capture)
        btnStop = findViewById(R.id.btn_stop)
        btnCapture = findViewById(R.id.btn_capture)
        surfaceView = findViewById(R.id.surface_view)

        btnStart.setOnClickListener {
            startCamera()
        }
        btnStop.setOnClickListener {
            stopCamera()
        }
        btnCapture.setOnClickListener {
            captureImage()
        }
        surfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(this)
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        rawCallback = Camera.PictureCallback { _, _ ->
            Log.d("Log", "OnPictureTaken - raw")
        }

        shutterCallback = Camera.ShutterCallback {
            Log.d("SurfaceViewActivity", "OnShutter'd")
        }

        jpegCallback = Camera.PictureCallback { _, _ ->
            val outStream: FileOutputStream?
            try {
                outStream = FileOutputStream(
                    String.format(
                        "/sdcard/%d.jpg", System.currentTimeMillis()
                    )
                )
                outStream.write(data)
                outStream.close()
                Log.d("SurfaceViewActivity", "onPictureTaken - wrote bytes: $data")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
            }
            Log.d("SurfaceViewActivity", "onPictureTaken - jpeg");
        }
    }

    private fun startCamera() {
        try {
            camera = Camera.open()
        } catch (e: RuntimeException) {
            Log.d("SurfaceViewActivity" , "initCamera() = $e")
            return
        }
        val params: Camera.Parameters = camera.parameters
        params.previewFrameRate = 20
        params.setPreviewSize(176, 144)
        camera.parameters = params
        try {
            camera.setPreviewDisplay(surfaceHolder)
            camera.startPreview()
        } catch (e: Exception) {
            Log.d("SurfaceViewActivity", "initCamera() = $e")
            return
        }
    }

    private fun stopCamera() {
        camera.stopPreview()
        camera.release()
    }

    private fun captureImage() {
        camera.takePicture(shutterCallback, rawCallback, jpegCallback)
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: SurfaceHolder?) {}

    override fun surfaceCreated(p0: SurfaceHolder?) {}

}