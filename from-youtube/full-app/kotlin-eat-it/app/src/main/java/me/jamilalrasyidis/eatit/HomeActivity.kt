package me.jamilalrasyidis.eatit

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class HomeActivity : AppCompatActivity() {

    private val database: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private var categories: CollectionReference? = null
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.rv_menu_list) }
    private val toolbar: Toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val drawer by lazy { findViewById<DrawerLayout>(R.id.drawer_layout) }
    private val toggle by lazy { ActionBarDrawerToggle(this@HomeActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        val tvFullName = headerView.findViewById<TextView>(R.id.tv_full_name)
        categories = database.collection("categories")

        setSupportActionBar(toolbar)
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        tvFullName.text = Common.currentUser.name

        categories!!.get().addOnSuccessListener {
            it.documents.forEach {category ->
//
            }
        }
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)

        loadMenu()
    }

    private fun loadMenu() {
        val options: FirestoreRecyclerOptions<Category> = FirestoreRecyclerOptions.Builder<Category>()
            .setQuery(categories!!, Category::class.java)
            .build()

        val adapter: FirestoreRecyclerAdapter<Category, MenuViewHolder> = object : FirestoreRecyclerAdapter<Category, MenuViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
                return MenuViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
                )
            }

            override fun onBindViewHolder(holder: MenuViewHolder, position: Int, model: Category) {
                Log.d(TAG, model.name+","+model.imageUrl)
                holder.tvMenuName.text = model.name
                Picasso.get().load(model.imageUrl).into(holder.ivMenuImage)
                val clickItem: Category = model
                holder.itemClickListener = object : ItemClickListener {
                    override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                        Toast.makeText(this@HomeActivity, clickItem.name, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TAG = "HomeActivity"
    }
}
