package me.jamilalrasyidis.figureoutcodeindnet.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import me.jamilalrasyidis.figureoutcodeindnet.R

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val setup: Setup

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(setup) {
            layoutRes?.let { setContentView(it) }

            runCatching {
                findViewById<Toolbar>(toolbarId!!)
            }.getOrElse {
                runCatching {
                    findViewById<Toolbar>(R.id.toolbar)
                }.getOrNull()
            }?.let {
                setSupportActionBar(it)
                if (enableToolbarBackbutton) {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }

                it.setNavigationOnClickListener { onNavigationClick() }
            }
        }
    }

    protected open fun onNavigationClick() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        if (isAnyFragmentHandleBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    protected fun isAnyFragmentHandleBackPressed() : Boolean {
        return true
    }
}