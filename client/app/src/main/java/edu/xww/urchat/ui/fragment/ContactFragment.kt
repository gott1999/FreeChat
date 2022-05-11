package edu.xww.urchat.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import edu.xww.urchat.R
import edu.xww.urchat.data.loader.LoaderManager
import edu.xww.urchat.ui.adapter.recyclerview.ContractAdapter
import edu.xww.urchat.data.runtime.RunTimeData

class ContactFragment(private val m_Context: Context) : BaseFragment(R.layout.fragment_contact) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onInit() {
        requireView().findViewById<TextView>(R.id.common_head_title).text = m_Context.getText(R.string.fragment_contact)
        setParams()
        setRefreshListener()
    }

    override fun onResume() {
        super.onResume()
        wake()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun wake() {
        recyclerView.adapter?.notifyDataSetChanged()
        Log.d("ContactFragment", "wakeup data:${RunTimeData.runTimeContacts.size}")
    }

    private fun setParams() {
        recyclerView = requireView().findViewById(R.id.fragment_contact_body_recycler_view)
        swipeRefreshLayout = requireView().findViewById(R.id.fragment_contact_swipe_refresh_layout)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = ContractAdapter(m_Context)
    }

    private fun setRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                LoaderManager.updateContact()
                swipeRefreshLayout.isRefreshing = false
            }, 1000)
        }
    }

}