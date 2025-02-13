package me.jamilalrasyidis.eatit.ui.home

import android.os.Bundle
import android.util.Log
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
import me.jamilalrasyidis.eatit.data.Food
import me.jamilalrasyidis.eatit.databinding.FragmentFoodListBinding

class FoodListFragment : Fragment() {

    private val database by lazy { FirebaseFirestore.getInstance() }

    private val foodRef by lazy { database.collection("foods") }

    private lateinit var binding: FragmentFoodListBinding

    private lateinit var foodListAdapter: FoodListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding                     = DataBindingUtil.inflate(inflater, R.layout.fragment_food_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFoodList()
    }

    private fun setupFoodList() {
        val categoryId = arguments?.getString("categoryId")?.toIntOrNull()
        val query                   = foodRef.orderBy("name", Query.Direction.ASCENDING)
                                             .whereEqualTo("categoryId", categoryId)
        val options                 = FirestoreRecyclerOptions.Builder<Food>().setQuery(query, Food::class.java).build()

        foodListAdapter             = FoodListAdapter(options)
        binding.rvFoodList.apply {
            setHasFixedSize(true)
            layoutManager           = LinearLayoutManager(requireContext())
            adapter                 = foodListAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        foodListAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        foodListAdapter.stopListening()
    }

    companion object {
        const val TAG = "FoodListFragment"
    }

}