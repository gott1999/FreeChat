package edu.xww.urchat.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R
import edu.xww.urchat.data.struct.Message
import edu.xww.urchat.helper.ImgHelper
import java.lang.IllegalArgumentException

class ChatMessageAdapter(private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class NormalTextViewHolder(view: View, viewType: Int, maxWidth: Int) :
        RecyclerView.ViewHolder(view) {
        val normaTextView: TextView = when (viewType) {
            Message.MessageType.RECEIVE_TEXT_NORMAL.ordinal -> view.findViewById(R.id.activity_chat_message_left_text)
            Message.MessageType.SEND_TEXT_NORMAL.ordinal -> view.findViewById(R.id.activity_chat_message_right_text)
            else -> throw IllegalArgumentException("Error argument during instancing 'NormalTextViewHolder'!")
        }

        init {
            normaTextView.maxWidth = maxWidth
        }
    }

    inner class NormalImageViewHolder(view: View, viewType: Int, maxWidth: Int, maxHeight: Int) :
        RecyclerView.ViewHolder(view) {
        val normaImageView: ImageView = when (viewType) {
            Message.MessageType.RECEIVE_TEXT_NORMAL.ordinal -> view.findViewById(R.id.activity_chat_message_left_text)
            Message.MessageType.SEND_TEXT_NORMAL.ordinal -> view.findViewById(R.id.activity_chat_message_right_text)
            else -> throw IllegalArgumentException("Error argument during instancing 'NormalImageViewHolder'!")
        }
    }

    override fun getItemViewType(position: Int): Int = messageList[position].typeId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // set max width
        val maxMessageWidth = (parent.context.resources.displayMetrics.widthPixels * 0.6).toInt()
        val maxMessageHeight =
            (parent.context.resources.displayMetrics.heightPixels * 0.6).toInt()

        // set view holder
        val view = when (viewType) {
            Message.MessageType.RECEIVE_TEXT_NORMAL.ordinal -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.activity_chat_message_left, parent, false)
            }
            Message.MessageType.SEND_TEXT_NORMAL.ordinal -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.activity_chat_message_right, parent, false)
            }
            else -> throw IllegalArgumentException("Unknown 'MessageType' during creating 'ViewHolder'!")
        }

        return when (viewType) {
            Message.MessageType.SEND_TEXT_NORMAL.ordinal -> NormalTextViewHolder(view, viewType, maxMessageWidth)
            Message.MessageType.RECEIVE_TEXT_NORMAL.ordinal -> NormalTextViewHolder(view, viewType, maxMessageWidth)
            Message.MessageType.SEND_IMAGE_NORMAL.ordinal -> NormalImageViewHolder(view, viewType, maxMessageWidth,maxMessageHeight)
            Message.MessageType.RECEIVE_IMAGE_NORMAL.ordinal -> NormalImageViewHolder(view, viewType, maxMessageWidth,maxMessageHeight)
            else -> throw IllegalArgumentException("Unknown 'MessageType' during creating 'ViewHolder'!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messageList[position]
        when (holder) {
            is NormalTextViewHolder -> holder.normaTextView.text = msg.content
            is NormalImageViewHolder -> ImgHelper.setImageBitmap(holder.normaImageView, msg.content)
            else -> throw  IllegalArgumentException("Call unknown ViewHolder type during binding ViewHolder!")
        }
    }

    override fun getItemCount(): Int = messageList.size


}