package edu.xww.urchat.data.runtime

import android.util.Log
import edu.xww.urchat.data.struct.user.LoggedInUser
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.network.source.LoginSource

object LoginStatus {

    /**
     * user data
     */
    var loggedInUser: LoggedInUser? = null
        private set

    /**
     * is logged in
     */
    fun isLoggedIn(): Boolean {
        return loggedInUser != null
    }

    /**
     * login in logic
     */
    fun login(server: String, port: Int,username: String, password: String): Result<LoggedInUser> {
        val result = LoginSource.login(server, port,username, password)
        if (result is Result.Success) {
            Log.d("Logic/Register", "${result.data.uniqueId} login success")
            loggedInUser = result.data
            RunTimeData.serverIp["main"] = server
            RunTimeData.serverPort["main"] = port
        } else {
            Log.d("Logic/Login", result.toString())
        }
        return result
    }




    /**
     * register in logic
     */
    fun register(server: String, port: Int,username: String, password: String): Result<LoggedInUser> {
        val result = LoginSource.register(server, port,username, password)
        if (result is Result.Success) {
            Log.d("Logic/Register", "${result.data.uniqueId} register success")
            loggedInUser = result.data
            RunTimeData.serverIp["main"] = server
            RunTimeData.serverPort["main"] = port
        } else {
            Log.d("Logic/Register", result.toString())
        }
        return result
    }

    /**
     * logout in logic
     */
    fun logout() {
        loggedInUser = null
        RunTimeData.clear()
        SecurityData.clear()
        val ip = RunTimeData.serverIp["main"]
        val port = RunTimeData.serverPort["main"]
        if (ip != null && port != null) {
            LoginSource.logout(ip, port)
        }
        Log.d("Logic/Logout", "User Logout")
    }



}