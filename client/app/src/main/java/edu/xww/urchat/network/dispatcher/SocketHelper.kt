package edu.xww.urchat.network.dispatcher

import edu.xww.urchat.data.runtime.RunTimeData
import edu.xww.urchat.network.proto.ProtocolOuterClass
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object SocketHelper {

    /**
     * 经典的一个请求一个返回
     * @param request ByteArray
     * @return string
     */
    fun sendRequest(request: ProtocolOuterClass.Protocol, ip: String, port: Int): ProtocolOuterClass.Protocol? {
        var socket: Socket? = null

        var res: ProtocolOuterClass.Protocol?

        try {
            socket = Socket(ip, port)

            val outputStream = socket.getOutputStream()
            outputStream.write(request.toByteArray())
            outputStream.flush()

            val inputStream = socket.getInputStream()
            res = ProtocolOuterClass.Protocol.parseFrom(inputStream)

        } catch (e: Exception) {
            throw Exception("Cant not connect to $ip:$port", e)
        } finally {
            socket?.close()
        }
        return res
    }

}