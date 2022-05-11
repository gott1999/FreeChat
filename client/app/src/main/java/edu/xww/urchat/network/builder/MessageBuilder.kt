package edu.xww.urchat.network.builder

import edu.xww.urchat.data.runtime.LoginStatus
import edu.xww.urchat.network.proto.UserMessageOuterClass

class MessageBuilder {

    enum class Type {
        // pure text
        TEXT,

        // image
        IMAGE,

        // position
        POSITION,

        // Binary
        BINARY,

        // add
        ADD,

        // del
        DEL,
    }

    private val builder = UserMessageOuterClass.UserMessage.newBuilder()

    init {
        builder.type = UserMessageOuterClass.MessageType.TEXT

        if (LoginStatus.loggedInUser != null)
            builder.src = LoginStatus.loggedInUser!!.uniqueId
        else
            builder.src = ""
    }

    fun build(): UserMessageOuterClass.UserMessage {
        builder.millisecondTimestamp = System.currentTimeMillis()
        return builder.build()
    }

    fun setDesUid(des_uid: String): MessageBuilder {
        builder.des = des_uid
        return this
    }

    fun setMessage(message: String): MessageBuilder {
        builder.message = message
        return this
    }

    fun setType(type: Type): MessageBuilder {
        when (type) {
            Type.TEXT -> builder.type = UserMessageOuterClass.MessageType.TEXT
            Type.IMAGE -> builder.type = UserMessageOuterClass.MessageType.IMAGE
            Type.POSITION -> builder.type = UserMessageOuterClass.MessageType.POSITION
            Type.BINARY -> builder.type = UserMessageOuterClass.MessageType.BINARY
            Type.ADD -> builder.type = UserMessageOuterClass.MessageType.ADD
            Type.DEL -> builder.type = UserMessageOuterClass.MessageType.DEL
        }
        return this
    }

}