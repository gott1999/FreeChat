package edu.xww.urchat.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import edu.xww.urchat.R
import edu.xww.urchat.adapter.recyclerview.MessageRecyclerAdapter
import edu.xww.urchat.data.RunTimeData

class MessageFragment(context: Context) : Fragment() {
    private val mContext = context
    private var isInit = false
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (!isInit) {
            setParams()
            setRefreshListener()
            isInit = true
        }
    }

    private fun setParams() {
        recyclerView = requireView().findViewById(R.id.fragment_message_recycler_view)
        swipeRefreshLayout = requireView().findViewById(R.id.fragment_message_swipe_refresh_layout)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = MessageRecyclerAdapter(mContext)
    }

    // TODO 上拉刷新资源
    @SuppressLint("NotifyDataSetChanged")
    private fun setRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                // update RunTimeData


                recyclerView.adapter?.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }, 1000)
        }
    }

}