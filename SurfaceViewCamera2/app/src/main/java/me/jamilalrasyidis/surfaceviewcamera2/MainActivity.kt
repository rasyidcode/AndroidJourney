package me.jamilalrasyidis.surfaceviewcamera2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.HandlerThread
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.ImageFormat
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.media.ImageReader.OnImageAvailableListener
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW
import android.view.SurfaceHolder
import android.hardware.camera2.CameraDevice
import java.util.Arrays.asList
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.params.StreamConfigurationMap
import android.R
import android.view.SurfaceView
import android.hardware.camera2.CameraManager
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.media.ImageReader
import android.os.Handler
import android.util.Size


class MainActivity : AppCompatActivity() {
    /** Output files will be saved as /sdcard/Pictures/cameratoo*.jpg  */
    val CAPTURE_FILENAME_PREFIX = "cameratoo"
    /** Tag to distinguish log prints.  */
    val TAG = "CameraToo"
    /** An additional thread for running tasks that shouldn't block the UI.  */
    private lateinit var mBackgroundThread: HandlerThread
    /** Handler for running tasks in the background.  */
    private lateinit var mBackgroundHandler: Handler
    /** Handler for running tasks on the UI thread.  */
    private lateinit var mForegroundHandler: Handler
    /** View for displaying the camera preview.  */
    private lateinit var mSurfaceView: SurfaceView
    /** Used to retrieve the captured image when the user takes a snapshot.  */
    private var mCaptureBuffer: ImageReader? = null
    /** Handle to the Android camera services.  */
    private lateinit var mCameraManager: CameraManager
    /** The specific camera device that we're using.  */
    private var mCamera: CameraDevice? = null
    /** Our image capture session.  */
    private var mCaptureSession: CameraCaptureSession? = null

    /**
     * Given `choices` of `Size`s supported by a camera, chooses the smallest one whose
     * width and height are at least as large as the respective requested values.
     * @param choices The list of sizes that the camera supports for the intended output class
     * @param width The minimum desired width
     * @param height The minimum desired height
     * @return The optimal `Size`, or an arbitrary one if none were big enough
     */
    fun chooseBigEnoughSize(choices: Array<Size>, width: Int, height: Int): Size {
        // Collect the supported resolutions that are at least as big as the preview Surface
        val bigEnough = ArrayList<Size>()
        for (option in choices) {
            if (option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option)
            }
        }
        // Pick the smallest of those, assuming we found any
        if (bigEnough.size > 0) {
            return Collections.min(bigEnough, CompareSizesByArea())
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size")
            return choices[0]
        }
    }

    /**
     * Compares two `Size`s based on their areas.
     */
    internal class CompareSizesByArea : Comparator<Size> {
        fun compare(lhs: Size, rhs: Size): Int {
            // We cast here to ensure the multiplications won't overflow
            return java.lang.Long.signum(lhs.getWidth() as Long * lhs.getHeight() - rhs.getWidth() as Long * rhs.getHeight())
        }
    }

    /**
     * Called when our `Activity` gains focus.
     *
     *Starts initializing the camera.
     */
    override fun onResume() {
        super.onResume()
        // Start a background thread to manage camera requests
        mBackgroundThread = HandlerThread("background")
        mBackgroundThread.start()
        mBackgroundHandler = Handler(mBackgroundThread.looper)
        mForegroundHandler = Handler(mainLooper)
        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        // Inflate the SurfaceView, set it as the main layout, and attach a listener
        val layout = layoutInflater.inflate(R.layout.mainactivity, null)
        mSurfaceView = layout.findViewById(R.id.mainSurfaceView)
        mSurfaceView.holder.addCallback(mSurfaceHolderCallback)
        setContentView(mSurfaceView)
        // Control flow continues in mSurfaceHolderCallback.surfaceChanged()
    }

    /**
     * Called when our `Activity` loses focus.
     *
     *Tears everything back down.
     */
    override fun onPause() {
        super.onPause()
        try {
            // Ensure SurfaceHolderCallback#surfaceChanged() will run again if the user returns
            mSurfaceView.holder.setFixedSize(/*width*/0, /*height*/0)
            // Cancel any stale preview jobs
            if (mCaptureSession != null) {
                mCaptureSession!!.close()
                mCaptureSession = null
            }
        } finally {
            if (mCamera != null) {
                mCamera!!.close()
                mCamera = null
            }
        }
        // Finish processing posted messages, then join on the handling thread
        mBackgroundThread.quitSafely()
        try {
            mBackgroundThread.join()
        } catch (ex: InterruptedException) {
            Log.e(TAG, "Background worker thread was interrupted while joined", ex)
        }

        // Close the ImageReader now that the background thread has stopped
        if (mCaptureBuffer != null) mCaptureBuffer!!.close()
    }

    /**
     * Called when the user clicks on our `SurfaceView`, which has ID `mainSurfaceView`
     * as defined in the `mainactivity.xml` layout file.
     *
     *Captures a full-resolution image
     * and saves it to permanent storage.
     */
    fun onClickOnSurfaceView(v: View) {
        if (mCaptureSession != /*handler*/null) {
            try {
                val requester = mCamera!!.createCaptureRequest(mCamera!!.TEMPLATE_STILL_CAPTURE)
                requester.addTarget(mCaptureBuffer!!.getSurface())
                try {
                    // This handler can be null because we aren't actually attaching any callback
                    mCaptureSession!!.capture(requester.build(), null, null)/*listener*/
                } catch (ex: CameraAccessException) {
                    Log.e(TAG, "Failed to file actual capture request", ex)
                }

            } catch (ex: CameraAccessException) {
                Log.e(TAG, "Failed to build actual capture request", ex)
            }

        } else {
            Log.e(TAG, "User attempted to perform a capture outside our session")
        }
        // Control flow continues in mImageCaptureListener.onImageAvailable()
    }

    /**
     * Callbacks invoked upon state changes in our `SurfaceView`.
     */
    val mSurfaceHolderCallback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        /** The camera device to use, or null if we haven't yet set a fixed surface size.  */
        private var mCameraId: String? = null
        /** Whether we received a change callback after setting our fixed surface size.  */
        private var mGotSecondCallback: Boolean = false

        override fun surfaceCreated(holder: SurfaceHolder) {
            // This is called every time the surface returns to the foreground
            Log.i(TAG, "Surface created")
            mCameraId = null
            mGotSecondCallback = false
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.i(TAG, "Surface destroyed")
            holder.removeCallback(this)
            // We don't stop receiving callbacks forever because onResume() will reattach us
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            // On the first invocation, width and height were automatically set to the view's size
            if (mCameraId == null) {
                // Find the device's back-facing camera and set the destination buffer sizes
                try {
                    for (cameraId in mCameraManager.cameraIdList) {
                        val cameraCharacteristics =
                            mCameraManager.getCameraCharacteristics(cameraId)
                        if (cameraCharacteristics.get<Int>(cameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                            Log.i(TAG, "Found a back-facing camera")
                            val info = cameraCharacteristics
                                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                            // Bigger is better when it comes to saving our image
                            val largestSize = Collections.max(
                                Arrays.asList(info!!.getOutputSizes(ImageFormat.JPEG)),
                                CompareSizesByArea()
                            )
                            // Prepare an ImageReader in case the user wants to capture images
                            Log.i(TAG, "Capture size: $largestSize")
                            mCaptureBuffer = ImageReader.newInstance(
                                largestSize.getWidth(),
                                largestSize.getHeight(), ImageFormat.JPEG, /*maxImages*/2
                            )
                            mCaptureBuffer!!.setOnImageAvailableListener(
                                mImageCaptureListener, mBackgroundHandler
                            )
                            // Danger, W.R.! Attempting to use too large a preview size could
                            // exceed the camera bus' bandwidth limitation, resulting in
                            // gorgeous previews but the storage of garbage capture data.
                            Log.i(
                                TAG, "SurfaceView size: " +
                                        mSurfaceView.width + 'x'.toString() + mSurfaceView.height
                            )
                            val optimalSize = chooseBigEnoughSize(
                                info.getOutputSizes(SurfaceHolder::class.java), width, height
                            )
                            // Set the SurfaceHolder to use the camera's largest supported size
                            Log.i(TAG, "Preview size: $optimalSize")
                            val surfaceHolder = mSurfaceView.holder
                            surfaceHolder.setFixedSize(
                                optimalSize.getWidth(),
                                optimalSize.getHeight()
                            )
                            mCameraId = cameraId
                            return
                            // Control flow continues with this method one more time
                            // (since we just changed our own size)
                        }
                    }
                } catch (ex: CameraAccessException) {
                    Log.e(TAG, "Unable to list cameras", ex)
                }

                Log.e(TAG, "Didn't find any back-facing cameras")
                // This is the second time the method is being invoked: our size change is complete
            } else if (!mGotSecondCallback) {
                if (mCamera != null) {
                    Log.e(TAG, "Aborting camera open because it hadn't been closed")
                    return
                }
                // Open the camera device
                try {
                    mCameraManager.openCamera(
                        mCameraId, mCameraStateCallback,
                        mBackgroundHandler
                    )
                } catch (ex: CameraAccessException) {
                    Log.e(TAG, "Failed to configure output surface", ex)
                }

                mGotSecondCallback = true
                // Control flow continues in mCameraStateCallback.onOpened()
            }
        }
    }
    /**
     * Calledbacks invoked upon state changes in our `CameraDevice`.
     *
     *These are run on
     * `mBackgroundThread`.
     */
    val mCameraStateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            Log.i(TAG, "Successfully opened camera")
            mCamera = camera
            try {
                val outputs = Arrays.asList(
                    mSurfaceView.holder.surface, mCaptureBuffer!!.getSurface()
                )
                camera.createCaptureSession(
                    outputs, mCaptureSessionListener,
                    mBackgroundHandler
                )
            } catch (ex: CameraAccessException) {
                Log.e(TAG, "Failed to create a capture session", ex)
            }

            // Control flow continues in mCaptureSessionListener.onConfigured()
        }

        override fun onDisconnected(camera: CameraDevice) {
            Log.e(TAG, "Camera was disconnected")
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Log.e(TAG, "State error on device '" + camera.id + "': code " + error)
        }
    }
    /**
     * Callbacks invoked upon state changes in our `CameraCaptureSession`.
     *
     *These are run on
     * `mBackgroundThread`.
     */
    val mCaptureSessionListener: CameraCaptureSession.StateCallback =
        object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                Log.i(TAG, "Finished configuring camera outputs")
                mCaptureSession = session
                val holder = mSurfaceView.holder
                if (holder != null) {
                    try {
                        // Build a request for preview footage
                        val requestBuilder =
                            mCamera!!.createCaptureRequest(mCamera!!.TEMPLATE_PREVIEW)
                        requestBuilder.addTarget(holder.surface)
                        val previewRequest = requestBuilder.build()
                        // Start displaying preview images
                        try {
                            session.setRepeatingRequest(
                                previewRequest,
                                null,
                                null
                            )/*listener*//*handler*/
                        } catch (ex: CameraAccessException) {
                            Log.e(TAG, "Failed to make repeating preview request", ex)
                        }

                    } catch (ex: CameraAccessException) {
                        Log.e(TAG, "Failed to build preview request", ex)
                    }

                } else {
                    Log.e(TAG, "Holder didn't exist when trying to formulate preview request")
                }
            }

            override fun onClosed(session: CameraCaptureSession) {
                mCaptureSession = null
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Log.e(TAG, "Configuration error on device '" + mCamera!!.id)
            }
        }
    /**
     * Callback invoked when we've received a JPEG image from the camera.
     */
    val mImageCaptureListener: ImageReader.OnImageAvailableListener =
        object : ImageReader.OnImageAvailableListener() {
            fun onImageAvailable(reader: ImageReader) {
                // Save the image once we get a chance
                mBackgroundHandler.post(CapturedImageSaver(reader.acquireNextImage()))
                // Control flow continues in CapturedImageSaver#run()
            }
        }

    /**
     * Deferred processor responsible for saving snapshots to disk.
     *
     *This is run on
     * `mBackgroundThread`.
     */
    internal class CapturedImageSaver(
        /** The image to save.  */
        private val mCapture: Image
    ) : Runnable {
        override fun run() {
            try {
                // Choose an unused filename under the Pictures/ directory
                val file = File.createTempFile(
                    CAPTURE_FILENAME_PREFIX, ".jpg",
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                    )
                )
                try {
                    FileOutputStream(file).use({ ostream ->
                        Log.i(
                            TAG, "Retrieved image is" +
                                    (if (mCapture.getFormat() === ImageFormat.JPEG) "" else "n't") + " a JPEG"
                        )
                        val buffer = mCapture.getPlanes()[0].getBuffer()
                        Log.i(
                            TAG, "Captured image size: " +
                                    mCapture.getWidth() + 'x' + mCapture.getHeight()
                        )
                        // Write the image out to the chosen file
                        val jpeg = ByteArray(buffer.remaining())
                        buffer.get(jpeg)
                        ostream.write(jpeg)
                    })
                } catch (ex: FileNotFoundException) {
                    Log.e(TAG, "Unable to open output file for writing", ex)
                } catch (ex: IOException) {
                    Log.e(TAG, "Failed to write the image to the output file", ex)
                }

            } catch (ex: IOException) {
                Log.e(TAG, "Unable to create a new output file", ex)
            } finally {
                mCapture.close()
            }
        }
    }
}
