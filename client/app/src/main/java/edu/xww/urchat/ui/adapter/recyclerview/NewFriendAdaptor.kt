package edu.xww.urchat.ui.adapter.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import edu.xww.urchat.R
import edu.xww.urchat.data.struct.Result

import edu.xww.urchat.data.struct.system.CommonRecyclerViewItem
import edu.xww.urchat.network.builder.MessageBuilder
import edu.xww.urchat.network.source.DataSource
import java.util.concurrent.locks.ReentrantLock


class NewFriendAdaptor(
    private val m_Context: Context,
    private val list: MutableList<CommonRecyclerViewItem>
) : CommonRecyclerViewAdaptor(m_Context, list) {

    private val lock = ReentrantLock()

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(item: CommonRecyclerViewItem, position: Int, view: View) {
        val popupMenu = PopupMenu(m_Context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_agree, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_positive -> {
                    if (!lock.isLocked) {
                        lock.lock()
                        Thread {
                            val res = DataSource.responseContact(item.uid)
                            if (res is Result.Success) {
                                // success
                                Log.d("ChatActivity", "Add '$item.uid'")
                                list.removeAt(position)
                            } else {
                                view.post {
                                    Toast.makeText(
                                        m_Context,
                                        R.string.send_failed,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    this.notifyDataSetChanged()
                                }
                            }
                        }.start()
                        lock.unlock()
                    } else {
                        Toast.makeText(m_Context, R.string.waitting, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    list.removeAt(position)
                    this.notifyDataSetChanged()
                }
            }

            return@setOnMenuItemClickListener false
        }
        popupMenu.show()
    }

    override fun onLongClick(item: CommonRecyclerViewItem, position: Int, layout: View): Boolean {
        return false
    }
}