package edu.xww.urchat.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import edu.xww.urchat.R
import edu.xww.urchat.adapter.recyclerview.MessageBoxAdapter

class MessageFragment(private val m_Context: Context) : BaseFragment(R.layout.fragment_message) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onInit() {
        setParams()
        setRefreshListener()
    }

    private fun setParams() {
        recyclerView = requireView().findViewById(R.id.fragment_message_recycler_view)
        swipeRefreshLayout = requireView().findViewById(R.id.fragment_message_swipe_refresh_layout)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = MessageBoxAdapter(m_Context)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                // TODO update RunTimeData


                recyclerView.adapter?.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }, 1000)
        }
    }

    private fun updateRunTimeData() {

    }


}