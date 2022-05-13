package edu.xww.urchat.network.source

import android.util.Log
import com.google.protobuf.ByteString
import edu.xww.urchat.data.runtime.SRuntimeData
import edu.xww.urchat.network.dispatcher.ClientDispatcher
import edu.xww.urchat.network.builder.ProtocBuilder
import edu.xww.urchat.network.proto.ProtocolOuterClass
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread


object SocketHelper {

    private const val DEFAULT_CLIENT_PORT = 25566
    private const val DEFAULT_CLIENT_TIMEOUT = 10 * 1000

    private var isListen = false

    /**
     * One Request One Response
     * @param request ByteArray
     * @return string
     */
    fun sendRequest(
        request: ProtocolOuterClass.Protocol,
        ip: String? = SRuntimeData.SIP["main"], port: Int? = SRuntimeData.SPort["main"]
    ): ProtocolOuterClass.Protocol? {
        val host = ip ?: "127.0.0.1"
        val pt = port ?: 25565

        Log.d("Socket", "Send Request.")

        var socket: Socket? = null

        val res: ProtocolOuterClass.Protocol?

        try {
            socket = Socket(host, pt)
            socket.soTimeout = 1000 * 10
            val outputStream = socket.getOutputStream()
            outputStream.write(request.toByteArray())
            outputStream.write("#".toByteArray())

            val inputStream = socket.getInputStream()
            val bs: ByteString = ByteString.readFrom(inputStream)
            res = ProtocolOuterClass.Protocol.parseFrom(bs.substring(0, bs.size() - 1))
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Cant not connect to $ip:$port", e)
        } finally {
            Log.d("Socket", "Socket close.")
            socket?.close()
        }
        return res
    }

    /**
     * start listener
     */
    fun startClientListener() {
        if (!isListen && !ServerSocketThread.isAlive) {
            isListen = true
            ServerSocketThread.start()
        }
    }

    /**
     * stop listener
     */
    fun stopClientListener() {
        isListen = false
    }

    /**
     * ServerSocketThread
     */
    private val ServerSocketThread = Thread {
        var serverSocket: ServerSocket? = null
        val builder = ProtocBuilder()
        builder.responsePositive()
        try {
            serverSocket = ServerSocket(DEFAULT_CLIENT_PORT)
            while (isListen) {
                val socket = serverSocket.accept()
                try {
                    // in coming transmission
                    val inputStream = socket.getInputStream()
                    val res = ProtocolOuterClass.Protocol.parseFrom(inputStream)

                    // send to client dispatcher
                    thread { ClientDispatcher.tick(res) }.start()

                    // response positive
                    val out = socket.getOutputStream()
                    out.write(builder.buildValid().toByteArray())
                    out.flush()
                } catch (e: java.lang.Exception) {

                } finally {
                    if (socket != null && !socket.isClosed) socket.close()
                }
            }

        } catch (e: java.lang.Exception) {

        } finally {
            if (serverSocket != null && !serverSocket.isClosed) serverSocket.close()
        }
    }

}