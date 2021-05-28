package me.jamilalrasyidis.cameraviewtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.otaliastudios.cameraview.controls.Preview
import android.view.ViewGroup
import com.otaliastudios.cameraview.filter.Filters
import sun.security.provider.certpath.BuildStep.BACK
import android.view.View.MeasureSpec.getMode
import android.content.Intent
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.CameraListener
import android.widget.Toast
import android.R.attr.start
import androidx.core.view.ViewCompat.getRotation
import androidx.core.view.ViewCompat.setRotation
import androidx.core.view.ViewCompat.setScaleY
import androidx.core.view.ViewCompat.setScaleX
import android.animation.ValueAnimator
import android.graphics.*
import android.view.ViewTreeObserver
import java.nio.file.Files.size
import java.util.Arrays.asList
import com.otaliastudios.cameraview.controls.Grid
import androidx.test.espresso.action.Tap
import com.otaliastudios.cameraview.controls.VideoCodec
import com.otaliastudios.cameraview.controls.Hdr
import com.otaliastudios.cameraview.controls.WhiteBalance
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Preview
import com.google.android.gms.common.util.IOUtils.toByteArray
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.view.View
import jdk.nashorn.internal.objects.NativeDate.getTime
import com.otaliastudios.cameraview.frame.FrameProcessor
import com.otaliastudios.cameraview.CameraLogger
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.frame.Frame
import java.io.ByteArrayOutputStream


class CameraActivity : AppCompatActivity(), View.OnClickListener, OptionView {

    private var camera: CameraView? = null
    private var controlPanel: ViewGroup? = null
    private var mCaptureTime: Long = 0

    private var mCurrentFilter = 0
    private val mAllFilters = Filters.values()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE)

        camera = findViewById(R.id.camera)
        camera!!.setLifecycleOwner(this)
        camera!!.addCameraListener(Listener())

        if (USE_FRAME_PROCESSOR) {
            camera!!.addFrameProcessor(object : FrameProcessor {
                private var lastTime = System.currentTimeMillis()

                fun process(frame: Frame) {
                    val newTime = frame.getTime()
                    val delay = newTime - lastTime
                    lastTime = newTime
                    LOG.e("Frame delayMillis:", delay, "FPS:", 1000 / delay)
                    if (DECODE_BITMAP) {
                        val yuvImage = YuvImage(
                            frame.getData(), ImageFormat.NV21,
                            frame.getSize().getWidth(),
                            frame.getSize().getHeight(),
                            null
                        )
                        val jpegStream = ByteArrayOutputStream()
                        yuvImage.compressToJpeg(
                            Rect(
                                0, 0,
                                frame.getSize().getWidth(),
                                frame.getSize().getHeight()
                            ), 100, jpegStream
                        )
                        val jpegByteArray = jpegStream.toByteArray()
                        val bitmap =
                            BitmapFactory.decodeByteArray(jpegByteArray, 0, jpegByteArray.size)

                        bitmap.toString()
                    }
                }
            })
        }

        findViewById<View>(R.id.edit).setOnClickListener(this)
        findViewById(R.id.capturePicture).setOnClickListener(this)
        findViewById(R.id.capturePictureSnapshot).setOnClickListener(this)
        findViewById(R.id.captureVideo).setOnClickListener(this)
        findViewById(R.id.captureVideoSnapshot).setOnClickListener(this)
        findViewById(R.id.toggleCamera).setOnClickListener(this)
        findViewById(R.id.changeFilter).setOnClickListener(this)

        controlPanel = findViewById(R.id.controls)
        val group = controlPanel!!.getChildAt(0) as ViewGroup
        val watermark = findViewById(R.id.watermark)

        val options = Arrays.asList(
            // Layout
            Option.Width(), Option.Height(),
            // Engine and preview
            Option.Mode(), Option.Engine(), Option.Preview(),
            // Some controls
            Option.Flash(), Option.WhiteBalance(), Option.Hdr(),
            Option.PictureMetering(), Option.PictureSnapshotMetering(),
            // Video recording
            Option.PreviewFrameRate(), Option.VideoCodec(), Option.Audio(),
            // Gestures
            Option.Pinch(), Option.HorizontalScroll(), Option.VerticalScroll(),
            Option.Tap(), Option.LongTap(),
            // Watermarks
            Option.OverlayInPreview(watermark),
            Option.OverlayInPictureSnapshot(watermark),
            Option.OverlayInVideoSnapshot(watermark),
            // Other
            Option.Grid(), Option.GridColor(), Option.UseDeviceOrientation()
        )
        val dividers = Arrays.asList(
            // Layout
            false, true,
            // Engine and preview
            false, false, true,
            // Some controls
            false, false, false, false, true,
            // Video recording
            false, false, true,
            // Gestures
            false, false, false, false, true,
            // Watermarks
            false, false, true,
            // Other
            false, false, true
        )
        for (i in options.indices) {
            val view = OptionView(this)

            view.setOption(options.get(i), this)
            view.setHasDivider(dividers.get(i))
            group.addView(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        controlPanel!!.viewTreeObserver.addOnGlobalLayoutListener {
            val b = BottomSheetBehavior.from(controlPanel!!)
            b.state = BottomSheetBehavior.STATE_HIDDEN
        }

        // Animate the watermark just to show we record the animation in video snapshots
        val animator = ValueAnimator.ofFloat(1f, 0.8f)
        animator.duration = 300
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.addUpdateListener { animation ->
            val scale = animation.animatedValue as Float
            watermark.setScaleX(scale)
            watermark.setScaleY(scale)
            watermark.setRotation(watermark.getRotation() + 2)
        }
        animator.start()
    }

    private fun message(content: String, important: Boolean) {
        if (important) {
            LOG.w(content)
            Toast.makeText(this, content, Toast.LENGTH_LONG).show()
        } else {
            LOG.i(content)
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
        }
    }

    private inner class Listener : CameraListener() {

        override fun onCameraOpened(options: CameraOptions) {
            val group = controlPanel!!.getChildAt(0) as ViewGroup
            for (i in 0 until group.childCount) {
                val view = group.getChildAt(i) as OptionView
                view.onCameraOpened(camera, options)
            }
        }

        override fun onCameraError(exception: CameraException) {
            super.onCameraError(exception)
            message("Got CameraException #" + exception.reason, true)
        }

        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            if (camera!!.isTakingVideo) {
                message("Captured while taking video. Size=" + result.size, false)
                return
            }

            // This can happen if picture was taken with a gesture.
            val callbackTime = System.currentTimeMillis()
            if (mCaptureTime == 0L) mCaptureTime = callbackTime - 300
            LOG.w("onPictureTaken called! Launching activity. Delay:", callbackTime - mCaptureTime)
            PicturePreviewActivity.setPictureResult(result)
            val intent = Intent(this@CameraActivity, PicturePreviewActivity::class.java)
            intent.putExtra("delay", callbackTime - mCaptureTime)
            startActivity(intent)
            mCaptureTime = 0
            LOG.w("onPictureTaken called! Launched activity.")
        }

        override fun onVideoTaken(result: VideoResult) {
            super.onVideoTaken(result)
            LOG.w("onVideoTaken called! Launching activity.")
            VideoPreviewActivity.setVideoResult(result)
            val intent = Intent(this@CameraActivity, VideoPreviewActivity::class.java)
            startActivity(intent)
            LOG.w("onVideoTaken called! Launched activity.")
        }

        override fun onVideoRecordingStart() {
            super.onVideoRecordingStart()
            LOG.w("onVideoRecordingStart!")
        }

        override fun onVideoRecordingEnd() {
            super.onVideoRecordingEnd()
            message("Video taken. Processing...", false)
            LOG.w("onVideoRecordingEnd!")
        }

        override fun onExposureCorrectionChanged(
            newValue: Float,
            bounds: FloatArray, @Nullable fingers: Array<PointF>?
        ) {
            super.onExposureCorrectionChanged(newValue, bounds, fingers)
            message("Exposure correction:$newValue", false)
        }

        override fun onZoomChanged(
            newValue: Float,
            bounds: FloatArray, @Nullable fingers: Array<PointF>?
        ) {
            super.onZoomChanged(newValue, bounds, fingers)
            message("Zoom:$newValue", false)
        }
    }

    fun onClick(view: View) {
        when (view.getId()) {
            R.id.edit -> edit()
            R.id.capturePicture -> capturePicture()
            R.id.capturePictureSnapshot -> capturePictureSnapshot()
            R.id.captureVideo -> captureVideo()
            R.id.captureVideoSnapshot -> captureVideoSnapshot()
            R.id.toggleCamera -> toggleCamera()
            R.id.changeFilter -> changeCurrentFilter()
        }
    }

    override fun onBackPressed() {
        val b = BottomSheetBehavior.from(controlPanel!!)
        if (b.state != BottomSheetBehavior.STATE_HIDDEN) {
            b.state = BottomSheetBehavior.STATE_HIDDEN
            return
        }
        super.onBackPressed()
    }

    private fun edit() {
        val b = BottomSheetBehavior.from(controlPanel!!)
        b.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun capturePicture() {
        if (camera!!.mode == Mode.VIDEO) {
            message("Can't take HQ pictures while in VIDEO mode.", false)
            return
        }
        if (camera!!.isTakingPicture) return
        mCaptureTime = System.currentTimeMillis()
        message("Capturing picture...", false)
        camera!!.takePicture()
    }

    private fun capturePictureSnapshot() {
        if (camera!!.isTakingPicture) return
        if (camera!!.preview != Preview.GL_SURFACE) {
            message("Picture snapshots are only allowed with the GL_SURFACE preview.", true)
            return
        }
        mCaptureTime = System.currentTimeMillis()
        message("Capturing picture snapshot...", false)
        camera!!.takePictureSnapshot()
    }

    private fun captureVideo() {
        if (camera!!.mode == Mode.PICTURE) {
            message("Can't record HQ videos while in PICTURE mode.", false)
            return
        }
        if (camera!!.isTakingPicture || camera!!.isTakingVideo) return
        message("Recording for 5 seconds...", true)
        camera!!.takeVideo(File(filesDir, "video.mp4"), 5000)
    }

    private fun captureVideoSnapshot() {
        if (camera!!.isTakingVideo) {
            message("Already taking video.", false)
            return
        }
        if (camera!!.preview != Preview.GL_SURFACE) {
            message("Video snapshots are only allowed with the GL_SURFACE preview.", true)
            return
        }
        message("Recording snapshot for 5 seconds...", true)
        camera!!.takeVideoSnapshot(File(filesDir, "video.mp4"), 5000)
    }

    private fun toggleCamera() {
        if (camera!!.isTakingPicture || camera!!.isTakingVideo) return
        when (camera!!.toggleFacing()) {
            Facing.BACK -> message("Switched to back camera!", false)

            Facing.FRONT -> message("Switched to front camera!", false)
        }
    }

    private fun changeCurrentFilter() {
        if (camera!!.preview != Preview.GL_SURFACE) {
            message("Filters are supported only when preview is Preview.GL_SURFACE.", true)
            return
        }
        if (mCurrentFilter < mAllFilters.size - 1) {
            mCurrentFilter++
        } else {
            mCurrentFilter = 0
        }
        val filter = mAllFilters[mCurrentFilter]
        message(filter.toString(), false)

        // Normal behavior:
        camera!!.filter = filter.newInstance()

        // To test MultiFilter:
        // DuotoneFilter duotone = new DuotoneFilter();
        // duotone.setFirstColor(Color.RED);
        // duotone.setSecondColor(Color.GREEN);
        // camera.setFilter(new MultiFilter(duotone, filter.newInstance()));
    }

    fun <T> onValueChanged(option: Option<T>, value: T, name: String): Boolean {
        if (option is Option.Width || option is Option.Height) {
            val preview = camera!!.preview
            val wrapContent = value as Int == ViewGroup.LayoutParams.WRAP_CONTENT
            if (preview == Preview.SURFACE && !wrapContent) {
                message(
                    "The SurfaceView preview does not support width or height changes. " + "The view will act as WRAP_CONTENT by default.",
                    true
                )
                return false
            }
        }
        option.set(camera, value)
        val b = BottomSheetBehavior.from(controlPanel!!)
        b.state = BottomSheetBehavior.STATE_HIDDEN
        message("Changed " + option.getName() + " to " + name, false)
        return true
    }

    //region Permissions

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var valid = true
        for (grantResult in grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED
        }
        if (valid && !camera!!.isOpened) {
            camera!!.open()
        }
    }

    companion object {

        private val LOG = CameraLogger.create("DemoApp")
        private val USE_FRAME_PROCESSOR = false
        private val DECODE_BITMAP = true
    }

    //endregion
}