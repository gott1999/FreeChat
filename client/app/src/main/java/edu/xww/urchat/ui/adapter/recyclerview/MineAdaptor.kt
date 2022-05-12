package edu.xww.urchat.ui.adapter.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import edu.xww.urchat.R
import edu.xww.urchat.data.runtime.LoginStatus
import edu.xww.urchat.data.struct.system.CommonRecyclerViewItem
import edu.xww.urchat.ui.activity.InfoActivity
import edu.xww.urchat.ui.activity.NewFriendActivity
import edu.xww.urchat.ui.activity.Welcome

class MineAdaptor(
    private val m_Context: Context,
    private val list: MutableList<CommonRecyclerViewItem>
) : CommonRecyclerViewAdaptor(m_Context, list) {

    @SuppressLint("CommitPrefEdits")
    override fun onClick(item: CommonRecyclerViewItem, position: Int, view: View) {
        when (item.displayName) {
            m_Context.getString(R.string.logged_out) -> {
                val data = m_Context.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                data.clear()
                data.apply()
                LoginStatus.logout()
                val intent = Intent(m_Context, Welcome::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                m_Context.startActivity(intent)
            }
            m_Context.getString(R.string.update_info) -> {
                val intent = Intent(m_Context, InfoActivity::class.java)
                m_Context.startActivity(intent)
            }
            m_Context.getString(R.string.newfriend) -> {
                val intent = Intent(m_Context, NewFriendActivity::class.java)
                m_Context.startActivity(intent)
            }
            else -> {}
        }
    }

    override fun onLongClick(item: CommonRecyclerViewItem, position: Int, layout: View): Boolean {
        when (item.displayName) {
            m_Context.getString(R.string.logged_out) -> {
                Toast.makeText(m_Context, R.string.logged_out, Toast.LENGTH_SHORT).show()
            }
            m_Context.getString(R.string.update_info) -> {
                Toast.makeText(m_Context, R.string.update_info, Toast.LENGTH_SHORT).show()
            }
            m_Context.getString(R.string.newfriend) -> {
                Toast.makeText(m_Context, R.string.newfriend, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
        return true
    }


}