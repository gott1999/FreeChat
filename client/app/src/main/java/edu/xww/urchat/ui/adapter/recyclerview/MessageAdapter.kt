package edu.xww.urchat.ui.adapter.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R
import edu.xww.urchat.data.runtime.RunTimeData
import edu.xww.urchat.data.loader.ResourcesLoader
import edu.xww.urchat.ui.activity.ChatActivity
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MessageAdapter.MessageRecyclerHolder>() {

    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private val stk = RunTimeData.runTimeMessage

    private val contacts = RunTimeData.runTimeContacts

    inner class MessageRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: ConstraintLayout = itemView.findViewById(R.id.recycler_item_layout)
        val logo: ImageView = itemView.findViewById(R.id.recycler_item_message_logo)
        val title: TextView = itemView.findViewById(R.id.recycler_item_message_title)
        val latestMessage: TextView =
            itemView.findViewById(R.id.recycler_item_message_latest_message)
        val latestTime: TextView = itemView.findViewById(R.id.recycler_item_message_latest_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageRecyclerHolder {
        return MessageRecyclerHolder(View.inflate(mContext, R.layout.fragment_message_items, null))
    }

    override fun onBindViewHolder(holder: MessageRecyclerHolder, position: Int) {
        val data = stk[position] ?: return
        val contact = contacts[data.uid] ?: return
        holder.title.text = contact.displayName
        holder.latestMessage.text = data.msg
        holder.latestTime.text = sdf.format(Date(data.time))
        holder.layout.setOnClickListener { ChatActivity.startInstance(mContext, data.uid) }
        holder.layout.setOnLongClickListener { onMessageBoxLongClicked() }
        ResourcesLoader.setImageBitmap(mContext, holder.logo, contact.icon)
    }

    override fun getItemCount(): Int = stk.size()

    private fun onMessageBoxLongClicked(): Boolean {
        return true
    }

}