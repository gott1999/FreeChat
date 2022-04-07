package edu.xww.urchat.data.struct

data class LoggedInUser(
    var status: Int = -1,
    var userId: String = "",
    var displayName: String = "",
    var key: String = ""
)