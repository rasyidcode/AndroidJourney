package me.jamilalrasyidis.learningcustomcamera

import android.util.Size
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class Utility {

    internal class CompareSizesByArea : Comparator<Size> {

        override fun compare(lhs: Size?, rhs: Size?): Int {
            return java.lang.Long.signum(lhs!!.width.toLong() * lhs.height - rhs!!.width.toLong() * rhs.height)
        }

        companion object {
            fun newInstance() : CompareSizesByArea {
                return CompareSizesByArea()
            }
        }

    }

    companion object {

        fun chooseOptimalSize(
            choices: Array<Size>, textureViewWidth: Int,
            textureViewHeight: Int, maxWidth: Int, maxHeight: Int, aspectRatio: Size
        ): Size {
            val bigEnough: ArrayList<Size> = arrayListOf()
            val notBigEnough: ArrayList<Size> = arrayListOf()
            val w = aspectRatio.width
            val h = aspectRatio.height
            for (option in choices) {
                if (option.width <= maxWidth && option.height <= maxHeight && option.height == option.width * h / w) {
                    if (option.width >= textureViewHeight && option.height >= textureViewHeight) {
                        bigEnough.add(option)
                    } else {
                        notBigEnough.add(option)
                    }
                }
            }

            return when {
                bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
                notBigEnough.size > 0 -> Collections.max(notBigEnough, CompareSizesByArea())
                else -> choices[0]
            }
        }
    }
}