package me.jamilalrasyidis.figureoutcodeindnet.base.activity

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

class Setup private constructor(
    @LayoutRes
    val layoutRes: Int?,

    @IdRes
    val toolbarId: Int?,

    val enableToolbarBackbutton: Boolean
) {

    class Builder {

        @LayoutRes
        private var layoutRes: Int? = null

        @IdRes
        private var toolbarId: Int? = null

        private var enableToolbarBackbutton: Boolean = false

        fun layout(@LayoutRes layoutRes: Int): Builder {
            this.layoutRes = layoutRes
            return this
        }

        fun toolbar(@IdRes toolbarId: Int) : Builder {
            this.toolbarId = toolbarId
            return this
        }

        fun enableToolbarBackButton() : Builder {
            this.enableToolbarBackbutton = true
            return this
        }

        fun build(): Setup = Setup(layoutRes, toolbarId, enableToolbarBackbutton)
    }

}