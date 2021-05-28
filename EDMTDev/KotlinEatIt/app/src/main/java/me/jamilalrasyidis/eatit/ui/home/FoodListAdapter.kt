package me.jamilalrasyidis.eatit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import me.jamilalrasyidis.eatit.ItemClickListener
import me.jamilalrasyidis.eatit.data.Food
import me.jamilalrasyidis.eatit.databinding.ItemFoodBinding

class FoodListAdapter(options: FirestoreRecyclerOptions<Food>) : FirestoreRecyclerAdapter<Food, FoodListAdapter.FoodListViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFoodBinding.inflate(inflater, parent, false)
        return FoodListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int, model: Food) {
        holder.bind(model)
    }

    class FoodListViewHolder(val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var onFoodItemListener: ItemClickListener

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(food: Food) {
            binding.apply {
                tvFoodName.text = food.name
                Picasso.get().load(food.image).into(ivFoodImage)
            }
            onFoodItemListener = object : ItemClickListener {
                override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                    Snackbar.make(binding.root, "${food.name} is selected !", Snackbar.LENGTH_SHORT).show()
                }

            }
        }

        override fun onClick(p0: View?) {
            onFoodItemListener.onClick(p0!!, adapterPosition, false)
        }

    }
}