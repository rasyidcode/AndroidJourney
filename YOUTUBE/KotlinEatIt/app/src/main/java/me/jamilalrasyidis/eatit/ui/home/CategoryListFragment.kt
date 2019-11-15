package me.jamilalrasyidis.eatit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import me.jamilalrasyidis.eatit.R
import me.jamilalrasyidis.eatit.data.Category
import me.jamilalrasyidis.eatit.databinding.FragmentCategoryListBinding

class CategoryListFragment : Fragment() {

    private lateinit var binding: FragmentCategoryListBinding

    private val database            by lazy { FirebaseFirestore.getInstance() }

    private val categories          by lazy { database.collection("categories") }

    private lateinit var adapter: CategoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding                     = DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val query: Query            = categories.orderBy("name", Query.Direction.DESCENDING)
        val options: FirestoreRecyclerOptions<Category> = FirestoreRecyclerOptions.Builder<Category>()
            .setQuery(query, Category::class.java)
            .build()

        adapter                     = CategoryListAdapter(options)
        binding.rvCategoryList.apply {
            setHasFixedSize(true)
            layoutManager           = LinearLayoutManager(requireContext())
            adapter                 = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    companion object {
        const val TAG = "CategoryListFragment"
    }
}
