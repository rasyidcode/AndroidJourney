package me.jamilalrasyidis.augmentrealitysample

import android.graphics.*
import android.graphics.drawable.Drawable

class PointerDrawable : Drawable() {

    private val paint: Paint = Paint()

    var enabled: Boolean = false

    override fun draw(p0: Canvas) {
       val cx: Float = (p0.width / 2).toFloat()
       val cy: Float = (p0.height / 2).toFloat()

        if (enabled) {
            paint.color = Color.RED
            p0.drawCircle(cx, cy, 10f, paint)
        } else {
            paint.color = Color.GREEN
            p0.drawText("X", cx, cy, paint)
        }
    }

    override fun setAlpha(p0: Int) {}

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(p0: ColorFilter?) {}

}