package com.rcd.bambang.daggerdota2stat

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcd.bambang.daggerdota2stat.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityHomeBinding>(
            this@HomeActivity,
            R.layout.activity_home
        )
    }

    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = "Home"

        favoritesAdapter = FavoritesAdapter(this@HomeActivity)
        binding.rvHome.layoutManager = LinearLayoutManager(this@HomeActivity)
        binding.rvHome.adapter = favoritesAdapter
        binding.rvHome.setHasFixedSize(true)

        @Suppress("DEPRECATION")
        supportLoaderManager.initLoader(0, null, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home_search -> {
                startActivity(Intent(this@HomeActivity, SearchActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            this@HomeActivity,
            DotaContract.DotaSubscriber.CONTENT_URI,
            null,
            null,
            null,
            DotaContract.DotaSubscriber._ID
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data?.count != 0) {
            binding.emptyView.visibility = View.GONE
            binding.rvHome.visibility = View.VISIBLE
        } else {
            binding.emptyView.visibility = View.VISIBLE
            binding.rvHome.visibility = View.GONE
        }
        favoritesAdapter.addData(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}

}