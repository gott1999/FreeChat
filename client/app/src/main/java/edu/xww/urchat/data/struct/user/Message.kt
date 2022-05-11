package edu.xww.urchat.data.struct.user


class Message private constructor(
    private val m_messageType: MessageType,
    private val m_content: String,
    public var time: Long = 0
) : java.io.Serializable {

    companion object {

        fun sendNormalText(string: String): Message =
            Message(MessageType.SEND_TEXT_NORMAL, string)

        fun receiveNormalText(string: String): Message =
            Message(MessageType.RECEIVE_TEXT_NORMAL, string)

        fun sendNormalImg(name: String): Message =
            Message(MessageType.SEND_IMAGE_NORMAL, name)

        fun receiveNormalImg(name: String): Message =
            Message(MessageType.RECEIVE_IMAGE_NORMAL, name)

    }

    fun isMe(): Boolean = when (m_messageType) {
        MessageType.SEND_TEXT_NORMAL -> true
        MessageType.SEND_IMAGE_NORMAL -> true
        else -> false
    }

    enum class MessageType {
        SEND_TEXT_NORMAL, RECEIVE_TEXT_NORMAL,
        SEND_IMAGE_NORMAL, RECEIVE_IMAGE_NORMAL
    }

    public val typeId = m_messageType.ordinal

    public val typeName = m_messageType.name

    public val content = m_content

    private var success = true

    public fun sendFailed() {
        success = false
    }

}