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
                val intent = Intent(m_Context, Welcome::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                m_Context.startActivity(intent)
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