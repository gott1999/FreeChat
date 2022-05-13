package edu.xww.urchat.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.xww.urchat.R
import edu.xww.urchat.data.runtime.SLoginStatus
import edu.xww.urchat.ui.adapter.recyclerview.MineAdaptor
import edu.xww.urchat.data.struct.system.CommonRecyclerViewItem

class MineFragment(private val m_Context: Context) : BaseFragment(R.layout.fragment_mine) {

    companion object {
        fun updateUserInfo(displayName: String) {
            list?.get(0)?.displayName = displayName
        }

        fun updateUserIcon(name: String) {
            list?.get(0)?.icon = name
        }

        var list: MutableList<CommonRecyclerViewItem>? = null
    }

    override fun onDestroy() {
        super.onDestroy()
        list = null
    }

    private lateinit var recyclerView: RecyclerView

    override fun onInit() {
        requireView().findViewById<TextView>(R.id.common_head_title).text =
            m_Context.getText(R.string.fragment_me)
        setParams()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun wake() {
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun setParams() {
        recyclerView = requireView().findViewById(R.id.fragment_mine_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        if (list == null) {
            getList()
        }
        recyclerView.adapter = MineAdaptor(m_Context, list!!)
    }

    private fun getList() {
        list = arrayListOf(
            CommonRecyclerViewItem(
                SLoginStatus.userBasicData?.icon ?: "",
                SLoginStatus.userBasicData?.displayName ?: "",
                m_Context.getString(R.string.info)
            ),
            CommonRecyclerViewItem(
                "",
                m_Context.getString(R.string.update_info),
                m_Context.getString(R.string.update_info)
            ),
            CommonRecyclerViewItem(
                "",
                m_Context.getString(R.string.newfriend),
                m_Context.getString(R.string.fragment_contact)
            ),
            CommonRecyclerViewItem(
                "",
                m_Context.getString(R.string.setting),
                m_Context.getString(R.string.system)
            ),
            CommonRecyclerViewItem(
                "",
                m_Context.getString(R.string.logged_out),
                m_Context.getString(R.string.system)
            )
        )
    }

}