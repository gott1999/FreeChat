package edu.xww.urchat.data.struct.user


class Message private constructor(
    private val m_messageType: MessageType,
    private val m_content: String,
    public var time: Long = 0
) : java.io.Serializable {

    companion object {

        fun sendNormalText(_message: String, _time: Long): Message =
            Message(MessageType.TEXT_SEND, _message, _time)

        fun receiveNormalText(_message: String, _time: Long): Message =
            Message(MessageType.TEXT_RECEIVE, _message, _time)

        fun sendNormalImg(_message: String, _time: Long): Message =
            Message(MessageType.IMAGE_SEND, _message, _time)

        fun receiveNormalImg(_message: String, _time: Long): Message =
            Message(MessageType.IMAGE_RECEIVE, _message, _time)

    }

    fun isMe(): Boolean = when (m_messageType) {
        MessageType.TEXT_SEND -> true
        MessageType.IMAGE_SEND -> true
        else -> false
    }

    enum class MessageType {
        TEXT_SEND, TEXT_RECEIVE,
        IMAGE_SEND, IMAGE_RECEIVE
    }

    public val typeId = m_messageType.ordinal

    public val typeName = m_messageType.name

    public val content = m_content

    private var success = true

    public fun sendFailed() {
        success = false
    }

}