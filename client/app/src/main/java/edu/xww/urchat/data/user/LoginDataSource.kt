package edu.xww.urchat.data.user

import edu.xww.urchat.data.struct.LoggedInUser
import edu.xww.urchat.data.struct.Result

class LoginDataSource {

    // TODO send login
    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {

            Result.Success(LoggedInUser(1, "00000001", "我费", "0721"))
        } catch (e: Throwable) {
            Result.Error(Exception("Login failed", e))
        }
    }

    // TODO logout
    fun logout() {


    }
}