package edu.xww.urchat.adapter.recyclerview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R
import edu.xww.urchat.data.runtime.RunTimeData
import edu.xww.urchat.helper.ImgHelper
import edu.xww.urchat.ui.activity.ChatActivity

class MessageAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MessageAdapter.MessageRecyclerHolder>() {

    private val stk = RunTimeData.runTimeMessage

    class MessageRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: ConstraintLayout = itemView.findViewById(R.id.recycler_item_layout)
        val logo: ImageView = itemView.findViewById(R.id.recycler_item_message_logo)
        val title: TextView = itemView.findViewById(R.id.recycler_item_message_title)
        val latestMessage: TextView =
            itemView.findViewById(R.id.recycler_item_message_latest_message)
        val latestTime: TextView = itemView.findViewById(R.id.recycler_item_message_latest_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageRecyclerHolder =
        MessageRecyclerHolder(View.inflate(mContext, R.layout.fragment_message_items, null))

    override fun onBindViewHolder(holder: MessageRecyclerHolder, position: Int) {
        val data = stk.get(position) ?: return
        holder.title.text = data.messageTitle
        holder.latestMessage.text = data.latestMessage
        holder.latestTime.text = data.latestTime
        holder.layout.setOnClickListener { ChatActivity.startInstance(mContext, data.messageId, data.messageTitle) }
        holder.layout.setOnLongClickListener { onMessageBoxLongClicked() }
        ImgHelper.setUserIcon(holder.logo, data.messageLogo)
    }

    override fun getItemCount(): Int = stk.size()

    private fun onMessageBoxLongClicked(): Boolean {
        return true
    }

}