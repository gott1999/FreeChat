package edu.xww.urchat.data.runtime

import edu.xww.urchat.data.struct.user.LoggedInUser
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.data.user.LoginDataSource

object LoginRepository {

    private val dataSource = LoginDataSource()

    var loggedInUser: LoggedInUser? = null
        private set

    fun isLoggedIn(): Boolean {
        return loggedInUser != null
    }

    fun logout() {
        loggedInUser = null
        dataSource.logout()
    }

    fun login(server: String, port: Int,username: String, password: String): Result<LoggedInUser> {
        val result = dataSource.login(server, port,username, password)
        if (result is Result.Success) {
            loggedInUser = result.data
            RunTimeData.serverIp["main"] = server
            RunTimeData.serverPort["main"] = port
        }
        return result
    }

    fun register(server: String, port: Int,username: String, password: String): Result<LoggedInUser> {
        // 未完成
        val result = dataSource.register(server, port,username, password)
        if (result is Result.Success) {
            loggedInUser = result.data
            RunTimeData.serverIp["main"] = server
            RunTimeData.serverPort["main"] = port
        }
        return result
    }

}