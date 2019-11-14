package me.jamilalrasyidis.surfaceviewcamera2b

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler
import android.os.Message
import android.view.Surface
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity(), SurfaceHolder.Callback, Handler.Callback {

    private val mHandler = Handler(this)

    private var mSurfaceView: SurfaceView? = null

    private var mSurfaceHolder: SurfaceHolder? = null

    private var mCameraManager: CameraManager? = null

    private var mCameraIDsList: Array<String>? = null

    private var mCameraStateCB: CameraDevice.StateCallback? = null

    private var mCameraDevice: CameraDevice? = null

    private var mCaptureSession: CameraCaptureSession? = null

    private var mSurfaceCreated = true

    private var mIsCameraConfigured = false

    private val mCameraSurface: Surface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSurfaceView = findViewById(R.id.sfv_main)
        mSurfaceHolder = mSurfaceView.holder
        mSurfaceHolder.addCallback(this)
        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleMessage(p0: Message): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val TAG = "CamTest"

        const val MY_PERMISSIONS_REQUEST_CAMERA = 1242

        private val MSG_CAMERA_OPENED = 1

        private val MSG_SURFACE_READY = 2
    }
}
