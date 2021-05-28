package me.jamilalrasyidis.introductiontosceneform

import android.graphics.*
import android.graphics.drawable.Drawable

class PointerDrawable : Drawable() {
    private val paint: Paint = Paint()
    var enabled: Boolean = false

    override fun draw(canvas: Canvas) {
        val cx: Float = (canvas.width / 2).toFloat()
        val cy: Float = (canvas.height / 2).toFloat()
        if (enabled) {
            paint.color = Color.GREEN
            canvas.drawCircle(cx, cy, 10f, paint)
        } else {
            paint.color = Color.GRAY
            canvas.drawText("X", cx, cy, paint)
        }
    }

    override fun setAlpha(p0: Int) {}

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(p0: ColorFilter?) {}

}