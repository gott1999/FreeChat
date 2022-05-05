package edu.xww.urchat.data.user

import edu.xww.urchat.data.runtime.RunTimeData
import edu.xww.urchat.data.runtime.SecurityData
import edu.xww.urchat.data.struct.user.LoggedInUser
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.network.dispatcher.SocketHelper
import edu.xww.urchat.network.proto.ProtocBuilder
import edu.xww.urchat.network.proto.ProtocolOuterClass

class LoginDataSource {

    fun login(server: String, port: Int, username: String, password: String): Result<LoggedInUser> {
        val pb = ProtocBuilder().loginMethod(username, password).build()
        return post(pb, server, port)
    }

    fun register(
        server: String,
        port: Int,
        username: String,
        password: String
    ): Result<LoggedInUser> {
        val pb = ProtocBuilder().registerMethod(username, password).build()
        return post(pb, server, port)
    }

    private fun post(
        protocol: ProtocolOuterClass.Protocol,
        server: String,
        port: Int
    ): Result<LoggedInUser> {
        return try {
            val rt = SocketHelper.sendRequest(protocol, server, port)
                ?: throw Exception("The message is empty!")

            if (!rt.valid) throw Exception("Error phone or password!")

            val mp = rt.pairsMap
            if (mp == null || !mp.containsKey("uid") || !mp.containsKey("displayName") || !mp.containsKey(
                    "key"
                )
            ) {
                throw Exception("Data lost!")
            }
            val uid = mp["uid"]!!
            val displayName = mp["displayName"]!!
            val key = mp["key"]!!

            Result.Success(LoggedInUser(0, uid, displayName, key))
        } catch (e: Throwable) {
            Result.Error(Exception(e))
        }
    }

    fun logout() {
        RunTimeData.clear()
        SecurityData.clear()
    }
}