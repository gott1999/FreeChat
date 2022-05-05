package edu.xww.urchat.network.proto

import com.google.protobuf.ByteString

class ProtocBuilder {
    private val builder = ProtocolOuterClass.Protocol.newBuilder()

    init {
        builder.putPairs("method", "NORMAL")
    }

    fun requestMode(): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.REQUEST
        return this
    }

    fun responseMode(): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.RESPONSE
        return this
    }

    fun transitMode(): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.TRANSIT
        return this
    }

    fun build(): ProtocolOuterClass.Protocol {
        builder.millisecondTimestamp = System.currentTimeMillis()
        builder.valid = true
        return builder.build()
    }

    fun putBytes(key: String, value: ByteString): ProtocBuilder {
        builder.putBits(key, value)
        return this
    }

    fun putsBytes(map: Map<String, ByteString>): ProtocBuilder {
        map.forEach { (key, value) -> builder.putBits(key, value) }
        return this
    }


    fun put(key: String, value: String): ProtocBuilder {
        builder.putPairs(key, value)
        return this
    }

    fun puts(map: Map<String, String>): ProtocBuilder {
        map.forEach { (key, value) -> builder.putPairs(key, value) }
        return this
    }

    fun loginMethod(uname: String, upassword: String): ProtocBuilder {
        builder.putPairs("method", "LOGIN")
        builder.putPairs("uname", uname)
        builder.putPairs("upassword", upassword)
        return this
    }

    fun registerMethod(uname: String, upassword: String): ProtocBuilder {
        builder.putPairs("method", "REGISTER")
        builder.putPairs("uname", uname)
        builder.putPairs("upassword", upassword)
        return this
    }

}