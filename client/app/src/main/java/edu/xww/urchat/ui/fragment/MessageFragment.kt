package edu.xww.urchat.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import edu.xww.urchat.R
import edu.xww.urchat.data.loader.SConversationLoader
import edu.xww.urchat.ui.adapter.recyclerview.MessageAdapter
import edu.xww.urchat.data.runtime.SRuntimeData

class MessageFragment(private val m_Context: Context) : BaseFragment(R.layout.fragment_message) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onInit() {
        requireView().findViewById<TextView>(R.id.common_head_title).text = m_Context.getText(R.string.app_name)
        setParams()
        setRefreshListener()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun wake() {
        SConversationLoader.updateMessage()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun setParams() {
        recyclerView = requireView().findViewById(R.id.fragment_message_recycler_view)
        swipeRefreshLayout = requireView().findViewById(R.id.fragment_message_swipe_refresh_layout)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = MessageAdapter(m_Context)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                SConversationLoader.updateMessage()
                swipeRefreshLayout.isRefreshing = false
                recyclerView.adapter?.notifyDataSetChanged()
            }, 1000)
        }
    }



}