package me.jamilalrasyidis.customspinner

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SpinnerAdapter(
    private val data: List<Hero>,
    activity: AppCompatActivity
) : BaseAdapter() {

    private var inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View? = convertView

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_list_hero, null)
        }

        val tvHeroName: TextView? = view?.findViewById(R.id.tv_hero_name)
        val tvHeroDesc: TextView? = view?.findViewById(R.id.tv_hero_desc)

        tvHeroName?.text = data[position].name
        tvHeroDesc?.text = data[position].desc
        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = super.getDropDownView(position, convertView, parent)
        val ll = view as LinearLayout
        val tvHeroName: TextView = ll.findViewById(R.id.tv_hero_name)
        val tvHeroDesc: TextView = ll.findViewById(R.id.tv_hero_desc)

        tvHeroName.gravity = Gravity.CENTER_HORIZONTAL
        tvHeroName.setTextColor(Color.parseColor("#ff0000"))
        tvHeroName.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        tvHeroDesc.gravity = Gravity.CENTER_HORIZONTAL
        tvHeroDesc.setTextColor(Color.parseColor("#ff00ff"))
        tvHeroDesc.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        return view
    }

}