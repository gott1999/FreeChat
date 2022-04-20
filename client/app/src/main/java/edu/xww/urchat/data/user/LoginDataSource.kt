package edu.xww.urchat.data.user

import edu.xww.urchat.data.runtime.RunTimeData
import edu.xww.urchat.data.runtime.SecurityData
import edu.xww.urchat.data.struct.user.LoggedInUser
import edu.xww.urchat.data.struct.Result

class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            Result.Success(LoggedInUser(0, "00000001", "我费", "0721"))
        } catch (e: Throwable) {
            Result.Error(Exception("Login failed", e))
        }
    }

    fun logout() {
        RunTimeData.clear()
        SecurityData.clear()
    }
}