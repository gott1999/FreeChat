package edu.xww.urchat.network.source

import android.util.Log
import edu.xww.urchat.data.struct.user.UserBasicData
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.network.builder.ProtocBuilder
import edu.xww.urchat.network.proto.ProtocolOuterClass

object LoginSource {

    /**
     * post login request
     */
    fun login(server: String, port: Int, username: String, password: String): Result<UserBasicData> {
        val pb = ProtocBuilder().requireLogin(username, password).buildValid()
        return post(pb, server, port)
    }

    /**
     * post register request
     */
    fun register(
        server: String,
        port: Int,
        username: String,
        password: String
    ): Result<UserBasicData> {
        val pb = ProtocBuilder().requireRegister(username, password).buildValid()
        return post(pb, server, port)
    }

    /**
     * post request
     */
    private fun post(
        pt: ProtocolOuterClass.Protocol,
        server: String,
        port: Int
    ): Result<UserBasicData> {
        return try {
            val rt = SocketHelper.sendRequest(pt, server, port) ?: return Result.Error("Can't connect to server")

            Log.d("LoginSource", "Get results")

            if (!rt.valid) return Result.Error(rt.getPairsOrDefault("ERROR", "Unknown Error"))

            val mp = rt.pairsMap
            if (mp == null ||
                !mp.containsKey("uid") ||
                !mp.containsKey("displayName") ||
                !mp.containsKey("key")
            ) {
                throw Exception("Data lost!")
            }

            Result.Success(UserBasicData(0, mp["uid"]!!, mp["displayName"]!!, mp["icon"]?:"", mp["key"]!!))
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }

    /**
     * post logout request
     */
    fun logout(server: String, port: Int) {
        Thread{
            val pb = ProtocBuilder().requireLogout().buildValid()
            SocketHelper.sendRequest(pb, server, port)
        }.start()
    }
}