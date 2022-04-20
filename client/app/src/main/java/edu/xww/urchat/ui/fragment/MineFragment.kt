package edu.xww.urchat.ui.fragment

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.xww.urchat.R
import edu.xww.urchat.adapter.recyclerview.MineAdaptor
import edu.xww.urchat.data.struct.system.CommonRecyclerViewItem

class MineFragment(private val m_Context: Context) : BaseFragment(R.layout.fragment_mine) {

    private lateinit var recyclerView: RecyclerView

    override fun onInit() {
        requireView().findViewById<TextView>(R.id.common_head_title).text =
            m_Context.getText(R.string.fragment_me)
        setParams()
    }

    private fun setParams() {
        recyclerView = requireView().findViewById(R.id.fragment_mine_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = MineAdaptor(
            m_Context, arrayListOf(
                CommonRecyclerViewItem("", m_Context.getString(R.string.fragment_me), "self"),
                CommonRecyclerViewItem("", m_Context.getString(R.string.logged_out), "system")
            )
        )
    }

}