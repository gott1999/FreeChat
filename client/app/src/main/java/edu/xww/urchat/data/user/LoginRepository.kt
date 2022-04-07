package edu.xww.urchat.data.user

import edu.xww.urchat.data.struct.LoggedInUser
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.data.runtime.UserData

class LoginRepository(private val dataSource: LoginDataSource) {

    fun logout() {
        UserData.loggedInUser = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Result<LoggedInUser> {
        val result = dataSource.login(username, password)
        if (result is Result.Success) setLoggedInUser(result.data)
        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        UserData.loggedInUser = loggedInUser
    }

}