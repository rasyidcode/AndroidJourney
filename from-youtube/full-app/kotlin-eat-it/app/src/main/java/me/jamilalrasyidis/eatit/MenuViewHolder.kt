package me.jamilalrasyidis.eatit

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    val ivMenuImage: ImageView by lazy { view.findViewById<ImageView>(R.id.iv_menu_image) }
    val tvMenuName: TextView by lazy { view.findViewById<TextView>(R.id.tv_menu_name) }
    var itemClickListener: ItemClickListener? = null

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        itemClickListener?.onClick(p0!!, adapterPosition, false)
    }

}