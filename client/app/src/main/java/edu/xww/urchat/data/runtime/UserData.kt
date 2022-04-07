package edu.xww.urchat.data.runtime

import edu.xww.urchat.data.struct.LoggedInUser

object UserData {

    var loggedInUser: LoggedInUser? = null

    val isLoggedIn: Boolean get() = loggedInUser != null

}