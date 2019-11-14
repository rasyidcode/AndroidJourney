package me.jamilalrasyidis.testthing.handler.post_office_simulator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.jamilalrasyidis.testthing.R
import java.util.*
import kotlin.collections.LinkedHashMap


class PostOfficeMainActivity : AppCompatActivity(), Client.ClientCallback {

    private val rvPostFeed by lazy { findViewById<RecyclerView>(R.id.rv_post_feed) }
    private lateinit var simulator: Simulator
    private lateinit var postOffice: PostOffice
    private lateinit var adapter: PostListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_office_main)

        rvPostFeed.layoutManager = LinearLayoutManager(this)
        adapter = PostListAdapter(LinkedList(), this)
        rvPostFeed.adapter = adapter
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        postOffice = PostOffice(LinkedHashMap())
        postOffice.start()
        simulator = Simulator(postOffice, this)
        simulator.createClients(10).start()
    }

    override fun onDestroy() {
        simulator.stop()
        postOffice.quit()
        super.onDestroy()
    }

    override fun onBackPressed() {}

    override fun onNewPost(receiver: Client, sender: Client, message: String) {
        runOnUiThread {
            val position = adapter.feedItemList.size
            adapter.feedItemList.add(
                PostListAdapter.FeedItem(
                    sender.name,
                    receiver.name,
                    message
                )
            )
            rvPostFeed.adapter?.notifyItemInserted(position)
            rvPostFeed.smoothScrollToPosition(position)
        }
    }

    class PostListAdapter(
        val feedItemList: LinkedList<FeedItem>,
        private val context: Context
    ) : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_post_feed, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return feedItemList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(feedItemList[position])
        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

            private val tvSenderName by lazy { v.findViewById<TextView>(R.id.tv_sender_name) }
            private val tvReceiverName by lazy { v.findViewById<TextView>(R.id.tv_receiver_name) }
            private val tvMessage by lazy { v.findViewById<TextView>(R.id.tv_message) }

            fun bind(feed: FeedItem) {
                tvSenderName.text = feed.senderName
                tvReceiverName.text = feed.receiverName
                tvMessage.text = feed.message
            }
        }

        class FeedItem(
            val senderName: String,
            val receiverName: String,
            val message: String
        )
    }
}