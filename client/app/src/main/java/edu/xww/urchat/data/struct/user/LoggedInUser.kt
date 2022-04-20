package edu.xww.urchat.data.struct.user

data class LoggedInUser(
    var status: Int = 0,
    var uniqueId: String = "",
    var displayName: String = "",
    var key: String = ""
)