package me.jamilalrasyidis.figureoutcodeindnet.base.fragment

import android.os.Handler
import androidx.fragment.app.Fragment

open class BaseFragment(contentLayoutId: Int = 0) : Fragment(contentLayoutId) {

    protected val handler by lazy { Handler(requireContext().mainLooper) }

    open fun onBackPressed(): Boolean = false

    protected fun postDelayed(callback: Runnable, delayMillis: Long) {
        handler.postDelayed(callback, delayMillis)
    }

    protected fun postDelayed(delayMillis: Long, action: () -> Unit): Runnable {
        val runnable = Runnable { action() }
        postDelayed(runnable, delayMillis)
        return runnable
    }

    protected fun post(callback: Runnable) {
        handler.post(callback)
    }

    protected fun post(action: () -> Unit): Runnable {
        val runnable = Runnable { action() }
        post(runnable)
        return runnable
    }
}