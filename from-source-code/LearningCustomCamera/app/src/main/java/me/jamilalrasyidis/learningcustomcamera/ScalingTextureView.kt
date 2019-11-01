package me.jamilalrasyidis.learningcustomcamera

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.TextureView

class ScalingTextureView : TextureView {

    private var ratioWidth = 0
    private var ratioHeight = 0
    private var screenWidth = 0
    private var screenHeight = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (ratioWidth == 0 || ratioHeight == 0) {
            setMeasuredDimension(width, height)
        }
        setMeasuredDimension(screenWidth, screenHeight)
    }

    fun setAspecRatio(_width: Int, _height: Int, _screenWidth: Int, _screenHeight: Int) {
        require(!(width < 0 || height < 0)) { "Size cannot be negative" }
        ratioWidth = _width
        ratioHeight = _height
        requestLayout()
        screenWidth = _screenWidth
        screenHeight = _screenHeight
    }

    companion object {
        const val TAG = "ScalingTextureView"
    }
}