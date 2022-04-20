package edu.xww.urchat.adapter.recyclerview

import android.content.Context
import android.content.Intent
import edu.xww.urchat.R
import edu.xww.urchat.data.runtime.LoginRepository
import edu.xww.urchat.data.struct.system.CommonRecyclerViewItem
import edu.xww.urchat.ui.activity.Welcome

class MineAdaptor(
    private val m_Context: Context,
    private val list: MutableList<CommonRecyclerViewItem>
) : CommonRecyclerViewAdaptor(m_Context, list) {

    override fun onBindViewHolder(holder: CommonRecyclerViewAdaptorHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.gap.text = ""
        holder.gap.height = 20
    }

    override fun onClick(item: CommonRecyclerViewItem) {
        when (item.displayName) {
            m_Context.getString(R.string.logged_out) -> {
                LoginRepository.logout()
                m_Context.startActivity(Intent(m_Context, Welcome::class.java))
            }
            else -> {}
        }
    }

    override fun onLongClick(item: CommonRecyclerViewItem): Boolean {
        when (item.displayName) {
            m_Context.getString(R.string.logged_out) -> {}
            else -> {}
        }
        return true
    }


}