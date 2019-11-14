package me.jamilalrasyidis.surfaceviewtuts

import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.io.*

class PictureActivity : AppCompatActivity() {

    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        imageView = findViewById(R.id.cam_result)
        imageView?.setImageBitmap(MainActivity2.bitmap)
//        saveImage(MainActivity2.bitmap)
    }

    private fun saveImage(bitmap: Bitmap?) : String {
        val bytes = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(Environment.getExternalStorageState() + IMAGE_DIRECTORY)
        if (wallpaperDirectory.exists()) {
            Log.d("PictureActivity", ""+wallpaperDirectory.mkdirs())
            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(wallpaperDirectory, "${Calendar.getInstance().timeInMillis}.jpg")
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("PictureActivity", "FileSaved --> ${f.absolutePath}")
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

    companion object {
        private const val IMAGE_DIRECTORY = "/CustomImage"
    }
}