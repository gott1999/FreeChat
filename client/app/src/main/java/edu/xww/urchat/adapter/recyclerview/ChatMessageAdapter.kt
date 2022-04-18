package edu.xww.urchat.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R
import edu.xww.urchat.data.struct.Message
import edu.xww.urchat.helper.ImgHelper
import java.lang.IllegalArgumentException

class ChatMessageAdapter(private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

        init {
            normaImageView.maxWidth = maxWidth
            normaImageView.maxWidth = maxHeight
        }
    }

    override fun getItemViewType(position: Int): Int = messageList[position].typeId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // set max width
        val maxMessageWidth = (parent.context.resources.displayMetrics.widthPixels * 0.6).toInt()
        val maxMessageHeight =
            (parent.context.resources.displayMetrics.heightPixels * 0.6).toInt()

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
        // icon
        ImgHelper.setImageBitmap(holder.icon, msg.content)
        // text
        holder.normaTextView.text = msg.content
    }

    private fun setImageViewHolder(holder: NormalImageViewHolder, position: Int) {
        val msg = messageList[position]
        // icon
        ImgHelper.setImageBitmap(holder.icon, msg.content)
        // img
        ImgHelper.setImageBitmap(holder.normaImageView, msg.content)
    }

}