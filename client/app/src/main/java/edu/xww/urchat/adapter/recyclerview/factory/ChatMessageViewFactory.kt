package edu.xww.urchat.adapter.recyclerview.factory

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import edu.xww.urchat.R
import edu.xww.urchat.data.struct.user.Message
import java.lang.IllegalArgumentException

class ChatMessageViewFactory {

    companion object {
        val instance = ChatMessageViewFactory()
    }

    fun getIcon(view: View,viewType: Int): ImageView = when (viewType) {
        Message.MessageType.RECEIVE_TEXT_NORMAL.ordinal -> view.findViewById(R.id.activity_chat_message_left_text_icon)
        Message.MessageType.SEND_TEXT_NORMAL.ordinal -> view.findViewById(R.id.activity_chat_message_right_text_icon)
        Message.MessageType.RECEIVE_TEXT_NORMAL.ordinal -> view.findViewById(R.id.activity_chat_message_left_img_icon)
        Message.MessageType.SEND_TEXT_NORMAL.ordinal -> view.findViewById(R.id.activity_chat_message_right_img_icon)
        else -> throw IllegalArgumentException("Error argument during instancing 'NormalIcon'!")
    }

    fun getNormaImageView(view: View,viewType: Int): ImageView = when (viewType) {
        Message.MessageType.RECEIVE_IMAGE_NORMAL.ordinal -> view.findViewById(R.id.activity_chat_message_left_img)
        Message.MessageType.SEND_IMAGE_NORMAL.ordinal -> view.findViewById(R.id.activity_chat_message_right_img)
        else -> throw IllegalArgumentException("Error argument during instancing 'NormalImageView'!")
    }

    fun getNormaTextView(view: View,viewType: Int): TextView = when (viewType) {
        Message.MessageType.RECEIVE_TEXT_NORMAL.ordinal -> view.findViewById(R.id.activity_chat_message_left_text)
        Message.MessageType.SEND_TEXT_NORMAL.ordinal -> view.findViewById(R.id.activity_chat_message_right_text)
        else -> throw IllegalArgumentException("Error argument during instancing 'NormalTextView'!")
    }

}


