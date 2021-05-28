package me.jamilalrasyidis.learningcustomcamera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import android.view.View


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "MainActivity"
        const val REQUEST_CODE: Int = 1234
    }

    private var permission: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        if (permission) {
            if (checkCameraHardware(this@MainActivity)) {
                startCamera2()
            } else {
                showSnackBar("you need a camera to use this application", Snackbar.LENGTH_INDEFINITE)
            }
        } else {
            verifyPermissions()
        }
    }

    private fun checkCameraHardware(context: Context) : Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun verifyPermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permissions.")

        val permissions =
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                permissions[0]
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                applicationContext,
                permissions[1]
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permission = true
            init()
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                permissions,
                REQUEST_CODE
            )
        }
    }

    private fun showSnackBar(text: String, length: Int) {
        val view = this.findViewById<View>(android.R.id.content).rootView
        Snackbar.make(view, text, length).show()
    }

    private fun startCamera2() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_camera_container, Camera2Fragment.newInstance(), getString(R.string.fragment_camera2))
            .commit()
    }
}
