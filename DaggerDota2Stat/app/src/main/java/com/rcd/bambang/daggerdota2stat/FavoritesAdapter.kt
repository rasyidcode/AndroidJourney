package com.rcd.bambang.daggerdota2stat

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rcd.bambang.daggerdota2stat.databinding.ItemPlayerCursorBinding

class FavoritesAdapter(private val context: Context) :
    RecyclerView.Adapter<FavoritesAdapter.BindingHolder>() {

    private var cursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding =
            ItemPlayerCursorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        this.cursor?.let { holder.bind(cursor = it, this.context) }
    }

    override fun getItemCount(): Int {
        return this.cursor?.count ?: 0
    }

    fun addData(cursor: Cursor?) {
        this.cursor = cursor
        notifyDataSetChanged()
    }

    inner class BindingHolder(private val binding: ItemPlayerCursorBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var id: String
        private lateinit var name: String
        private lateinit var url: String

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(cursor: Cursor, context: Context) {
            cursor.moveToPosition(adapterPosition)

            id = cursor.getString(cursor.getColumnIndex(DotaContract.DotaSubscriber.COLUMN_ACC_ID))
            name =
                cursor.getString(cursor.getColumnIndex(DotaContract.DotaSubscriber.COLUMN_PLAYER_NAME))
            url =
                cursor.getString(cursor.getColumnIndex(DotaContract.DotaSubscriber.COLUMN_AVATAR_URL))

            Log.v(TAG, "FavoritesAdapter: ${cursor.count}, id: $id, name: $name, url: $url")

            binding.tvPlayerId.text = id
            binding.tvPlayerName.text = name
            Glide.with(context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivPlayerAvatar)

        }

        override fun onClick(v: View?) {
            val player = Player(id.toLong(), name, url)
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("player-parcelable", player)
            context.startActivity(intent)
        }

    }

    companion object {
        const val TAG = "FavoritesAdapter"
    }
}