package edu.xww.urchat.ui.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R
import edu.xww.urchat.ui.adapter.recyclerview.factory.ChatMessageViewFactory
import edu.xww.urchat.data.struct.user.Message
import edu.xww.urchat.data.loader.SImageLoader
import edu.xww.urchat.data.runtime.SLoginStatus
import edu.xww.urchat.data.runtime.SRuntimeData
import java.lang.IllegalArgumentException

class ChatMessageAdapter(private val m_Context: Context, private val tarUid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        var maxWidth = 300
        var maxHeight = 400
    }

    private val messageList = SRuntimeData.SMessageList[tarUid]

    open inner class BaseViewHolder(view: View, viewType: Int) : RecyclerView.ViewHolder(view) {
        val icon = ChatMessageViewFactory.instance.getIcon(view, viewType)
    }

    inner class TextViewHolder(view: View, viewType: Int) :
        BaseViewHolder(view, viewType) {
        val normaTextView = ChatMessageViewFactory.instance.getNormaTextView(view, viewType)

        init {
            normaTextView.maxWidth = maxWidth
        }
    }

    inner class ImageViewHolder(view: View, viewType: Int) :
        BaseViewHolder(view, viewType) {
        val normaImageView = ChatMessageViewFactory.instance.getNormaImageView(view, viewType)
    }

    override fun getItemViewType(position: Int): Int = messageList[position].typeId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // set max width
        maxWidth = (parent.context.resources.displayMetrics.widthPixels * 0.6).toInt()
        maxHeight = (parent.context.resources.displayMetrics.heightPixels * 0.6).toInt()

        // get ViewHolder
        return getViewHolder(getView(parent, viewType), viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is TextViewHolder -> setTextViewHolder(holder, position)
        is ImageViewHolder -> setImageViewHolder(holder, position)
        else -> throw  IllegalArgumentException("Call unknown ViewHolder type during binding ViewHolder!")
    }

    override fun getItemCount(): Int = messageList.size

    private fun getView(parent: ViewGroup, viewType: Int) = when (viewType) {
        Message.MessageType.TEXT_RECEIVE.ordinal -> {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_chat_message_reveive_text, parent, false)
        }
        Message.MessageType.TEXT_SEND.ordinal -> {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_chat_message_send_text, parent, false)
        }
        Message.MessageType.IMAGE_RECEIVE.ordinal -> {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_chat_message_receive_img, parent, false)
        }
        Message.MessageType.IMAGE_SEND.ordinal -> {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_chat_message_send_img, parent, false)
        }
        else -> throw IllegalArgumentException("Unknown 'MessageType' during creating 'ViewHolder'!")
    }

    private fun getViewHolder(view: View, viewType: Int) = when (viewType) {
        Message.MessageType.TEXT_SEND.ordinal -> TextViewHolder(view, viewType,)
        Message.MessageType.TEXT_RECEIVE.ordinal -> TextViewHolder(view, viewType,)
        Message.MessageType.IMAGE_SEND.ordinal -> ImageViewHolder(view, viewType)
        Message.MessageType.IMAGE_RECEIVE.ordinal -> ImageViewHolder(view, viewType)
        else -> throw IllegalArgumentException("Unknown 'MessageType' during creating 'ViewHolder'!")
    }

    private fun setTextViewHolder(holder: TextViewHolder, position: Int) {
        val msg = messageList[position]
        // text
        holder.normaTextView.text = msg.content

        // icon
        val icon = if (msg.isMe()) SLoginStatus.userBasicData!!.icon
                    else SRuntimeData.SContacts[tarUid]!!.icon
        SImageLoader.setImageView(m_Context, holder.icon, icon)
    }

    private fun setImageViewHolder(holder: ImageViewHolder, position: Int) {
        val msg = messageList[position]

        // icon
        val icon = if (msg.isMe()) SLoginStatus.userBasicData!!.icon
                    else SRuntimeData.SContacts[tarUid]!!.icon
        SImageLoader.setImageView(m_Context, holder.icon, icon)

        // img
        SImageLoader.setImageView(m_Context, holder.normaImageView, msg.content, maxWidth, maxHeight)
    }

}