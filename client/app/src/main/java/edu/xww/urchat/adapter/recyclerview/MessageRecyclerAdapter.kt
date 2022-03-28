package edu.xww.urchat.adapter.recyclerview

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R
import edu.xww.urchat.data.RunTimeData
import edu.xww.urchat.helper.ImgHelper

class MessageRecyclerAdapter(context: Context): RecyclerView.Adapter<MessageRecyclerAdapter.MessageRecyclerHolder>(){
    private var count = 0
    private val mContext = context
    private val stk = RunTimeData.RunTimeMessageBox

    class MessageRecyclerHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val layout: ConstraintLayout = itemView.findViewById(R.id.recycler_item_layout)
        val logo: ImageView = itemView.findViewById(R.id.recycler_item_message_logo)
        val title: TextView = itemView.findViewById(R.id.recycler_item_message_title)
        val latestMessage: TextView = itemView.findViewById(R.id.recycler_item_message_latest_message)
        val latestTime: TextView = itemView.findViewById(R.id.recycler_item_message_latest_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageRecyclerHolder {
        return MessageRecyclerHolder(View.inflate(mContext, R.layout.recycle_item_message, null))
    }

    override fun onBindViewHolder(holder: MessageRecyclerHolder, position: Int) {
        val data = stk.get(position) ?: return
        holder.title.text = data.messageTitle
        holder.latestMessage.text = data.latestMessage
        holder.latestTime.text = data.latestTime
        holder.layout.setOnClickListener {
            Toast.makeText(mContext, data.messageId, Toast.LENGTH_SHORT).show()
        }
        ImgHelper.setUserIcon(holder.logo, data.messageLogo)
    }

    override fun getItemCount(): Int { return stk.size() }


}