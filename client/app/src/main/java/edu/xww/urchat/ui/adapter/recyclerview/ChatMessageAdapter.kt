package edu.xww.urchat.ui.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R
import edu.xww.urchat.ui.adapter.recyclerview.factory.ChatMessageViewFactory
import edu.xww.urchat.data.struct.user.Message
import edu.xww.urchat.data.loader.ResourcesLoader
import edu.xww.urchat.data.runtime.LoginStatus
import edu.xww.urchat.data.runtime.RunTimeData
import java.lang.IllegalArgumentException

class ChatMessageAdapter(private val m_Context: Context, private val tarUid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        var maxWidth = 80
        var maxHeight = 80
    }

    private val messageList = RunTimeData.runTimeMessageList[tarUid]!!

    open inner class BaseViewHolder(view: View, viewType: Int) : RecyclerView.ViewHolder(view) {
        val icon = ChatMessageViewFactory.instance.getIcon(view, viewType)
    }

    inner class NormalTextViewHolder(view: View, viewType: Int, maxWidth: Int) :
        BaseViewHolder(view, viewType) {
        val normaTextView = ChatMessageViewFactory.instance.getNormaTextView(view, viewType)

        init {
            normaTextView.maxWidth = maxWidth
        }
    }

    inner class NormalImageViewHolder(view: View, viewType: Int, maxWidth: Int, maxHeight: Int) :
        BaseViewHolder(view, viewType) {
        val normaImageView = ChatMessageViewFactory.instance.getNormaImageView(view, viewType)
    }

    override fun getItemViewType(position: Int): Int = messageList[position].typeId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // set max width
        val maxMessageWidth = (parent.context.resources.displayMetrics.widthPixels * 0.6).toInt()
        val maxMessageHeight =
            (parent.context.resources.displayMetrics.heightPixels * 0.6).toInt()
        maxWidth = maxMessageWidth
        maxHeight = maxMessageHeight

        // get ViewHolder
        return getViewHolder(getView(parent, viewType), viewType, maxMessageWidth, maxMessageHeight)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is NormalTextViewHolder -> setTextViewHolder(holder, position)
        is NormalImageViewHolder -> setImageViewHolder(holder, position)
        else -> throw  IllegalArgumentException("Call unknown ViewHolder type during binding ViewHolder!")
    }

    override fun getItemCount(): Int = messageList.size

    private fun getView(parent: ViewGroup, viewType: Int) = when (viewType) {
        Message.MessageType.RECEIVE_TEXT_NORMAL.ordinal -> {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_chat_message_left_text, parent, false)
        }
        Message.MessageType.SEND_TEXT_NORMAL.ordinal -> {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_chat_message_right_text, parent, false)
        }
        Message.MessageType.RECEIVE_IMAGE_NORMAL.ordinal -> {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_chat_message_left_img, parent, false)
        }
        Message.MessageType.SEND_IMAGE_NORMAL.ordinal -> {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_chat_message_right_img, parent, false)
        }
        else -> throw IllegalArgumentException("Unknown 'MessageType' during creating 'ViewHolder'!")
    }

    private fun getViewHolder(
        view: View,
        viewType: Int,
        maxMessageWidth: Int,
        maxMessageHeight: Int
    ) = when (viewType) {
        Message.MessageType.SEND_TEXT_NORMAL.ordinal -> NormalTextViewHolder(
            view,
            viewType,
            maxMessageWidth
        )
        Message.MessageType.RECEIVE_TEXT_NORMAL.ordinal -> NormalTextViewHolder(
            view,
            viewType,
            maxMessageWidth
        )
        Message.MessageType.SEND_IMAGE_NORMAL.ordinal -> NormalImageViewHolder(
            view,
            viewType,
            maxMessageWidth,
            maxMessageHeight
        )
        Message.MessageType.RECEIVE_IMAGE_NORMAL.ordinal -> NormalImageViewHolder(
            view,
            viewType,
            maxMessageWidth,
            maxMessageHeight
        )
        else -> throw IllegalArgumentException("Unknown 'MessageType' during creating 'ViewHolder'!")
    }

    private fun setTextViewHolder(holder: NormalTextViewHolder, position: Int) {
        val msg = messageList[position]
        val icon = if (msg.isMe())
            LoginStatus.loggedInUser!!.icon
        else
            RunTimeData.runTimeContacts[tarUid]!!.icon
        // icon
        ResourcesLoader.setImageBitmap(m_Context, holder.icon, icon)
        // text
        holder.normaTextView.text = msg.content
    }

    private fun setImageViewHolder(holder: NormalImageViewHolder, position: Int) {
        val msg = messageList[position]
        val icon = if (msg.isMe())
            LoginStatus.loggedInUser!!.icon
        else
            RunTimeData.runTimeContacts[tarUid]!!.icon

        // icon
        ResourcesLoader.setImageBitmap(m_Context, holder.icon, icon)

        // img
        ResourcesLoader.setBigImage(m_Context, holder.normaImageView, msg.content)
    }

}