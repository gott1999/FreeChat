package edu.xww.urchat.data

data class MessageBox(
    var messageId: String = "-1",
    var messageLogo: String = "Lost internet",
    var messageTitle: String = "Lost internet",
    var latestMessage: String = "Lost internet",
    var latestTime: String = "00:00"
) : java.io.Serializable
