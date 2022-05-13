package edu.xww.urchat.data.struct.user

data class UserBasicData(
    var status: Int = 0,
    var uniqueId: String = "",
    var displayName: String = "",
    var icon: String = "",
    var key: String = ""
)