package edu.xww.urchat

import edu.xww.urchat.network.proto.ProtocolOuterClass
import org.junit.Test
import java.lang.Exception

class ProtocTest {

    @Test
    fun pTest() {
        val builder = ProtocolOuterClass.Protocol.newBuilder()
        builder.valid = true
        builder.method = ProtocolOuterClass.ConnectMethod.LOGIN
        builder.putPairs("uname", "像威威")
        builder.putPairs("upassword", "1111ass")
        val data = builder.build()
        val str = data.toByteString()
        println(str)

        try {
            val n = ProtocolOuterClass.Protocol.parseFrom(str)
            println(n)
        } catch (e: Exception) {
            println(e.message)
        }

    }
}