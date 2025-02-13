package me.jamilalrasyidis.eatit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso
import me.jamilalrasyidis.eatit.ItemClickListener
import me.jamilalrasyidis.eatit.R
import me.jamilalrasyidis.eatit.data.Category
import me.jamilalrasyidis.eatit.databinding.ItemCategoryBinding

class CategoryListAdapter(options: FirestoreRecyclerOptions<Category>) : FirestoreRecyclerAdapter<Category, CategoryListAdapter.CategoryListViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)

        return CategoryListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int, model: Category) {
        holder.bind(model, snapshots.getSnapshot(position).id)
    }

    inner class CategoryListViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var itemClickListener: ItemClickListener

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(category: Category, categoryId: String) {
            binding.apply {
                tvCategoryName.text = category.name
                Picasso.get().load(category.image).into(ivCategoryImage)
                itemClickListener = object : ItemClickListener {
                    override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                        val navHost = Navigation.findNavController(binding.root)
                        val bundle = bundleOf("categoryId" to categoryId)

                        navHost.navigate(R.id.action_categoryListFragment_to_foodListFragment, bundle)
                    }
                }
            }
        }

        override fun onClick(p0: View?) {
            itemClickListener.onClick(p0!!, adapterPosition, false)
        }
    }

}