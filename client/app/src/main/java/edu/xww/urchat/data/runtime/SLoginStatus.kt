package edu.xww.urchat.data.runtime

import edu.xww.urchat.data.struct.user.UserBasicData
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.network.source.LoginSource

object SLoginStatus {

    /**
     * User data.
     */
    var userBasicData: UserBasicData? = null
        private set

    /**
     * Is logged in.
     */
    fun isLoggedIn(): Boolean {
        return userBasicData != null
    }

    /**
     * Login in logic.
     */
    fun login(
        server: String,
        port: Int,
        username: String,
        password: String
    ): Result<UserBasicData> {
        val result = LoginSource.login(server, port, username, password)
        if (result is Result.Success) {
            userBasicData = result.data
            SRuntimeData.SIP["main"] = server
            SRuntimeData.SPort["main"] = port
        }
        return result
    }

    /**
     * Register in logic
     */
    fun register(
        server: String,
        port: Int,
        username: String,
        password: String
    ): Result<UserBasicData> {
        val result = LoginSource.register(server, port, username, password)
        if (result is Result.Success) {
            userBasicData = result.data
            SRuntimeData.SIP["main"] = server
            SRuntimeData.SPort["main"] = port
        }
        return result
    }

    /**
     * logout in logic
     */
    fun logout() {
        // Logout must be later than destroying the IP and PORT
        val ip = SRuntimeData.SIP["main"]
        val port = SRuntimeData.SPort["main"]
        if (ip != null && port != null) LoginSource.logout(ip, port)

        userBasicData = null
        SecurityData.clear()
        SRuntimeData.clear()
    }

}