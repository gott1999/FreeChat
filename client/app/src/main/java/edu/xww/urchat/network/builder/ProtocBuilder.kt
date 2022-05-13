package edu.xww.urchat.network.builder

import com.google.protobuf.ByteString
import edu.xww.urchat.data.runtime.SLoginStatus
import edu.xww.urchat.network.proto.ProtocolOuterClass
import edu.xww.urchat.network.proto.UserMessageOuterClass

class ProtocBuilder {

    private val builder = ProtocolOuterClass.Protocol.newBuilder()

    init {
        if (SLoginStatus.userBasicData != null)
            builder.putConfig("UID", SLoginStatus.userBasicData!!.uniqueId)
        else
            builder.putConfig("UID", "VISITOR")
    }

    fun buildValid(): ProtocolOuterClass.Protocol {
        builder.millisecondTimestamp = System.currentTimeMillis()
        builder.valid = true
        return builder.build()
    }

    fun buildInvalid(): ProtocolOuterClass.Protocol {
        builder.millisecondTimestamp = System.currentTimeMillis()
        builder.valid = false
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

    fun clearBytes(): ProtocBuilder {
        builder.clearBits()
        return this
    }

    fun putPairs(key: String, value: String): ProtocBuilder {
        builder.putPairs(key, value)
        return this
    }

    fun putPairs(map: Map<String, String>): ProtocBuilder {
        map.forEach { (key, value) -> builder.putPairs(key, value) }
        return this
    }

    fun clearPairs(): ProtocBuilder {
        builder.clearPairs()
        return this
    }

    fun requireLogin(uname: String, upassword: String): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.REQUEST
        builder.putConfig("REQUIRE", "LOGIN")
        builder.putPairs("uname", uname)
        builder.putPairs("upassword", upassword)
        return this
    }

    fun requireRegister(uname: String, upassword: String): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.REQUEST
        builder.putConfig("REQUIRE", "REGISTER")
        builder.putPairs("uname", uname)
        builder.putPairs("upassword", upassword)
        return this
    }

    fun requireUpdate(): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.REQUEST
        builder.putConfig("REQUIRE", "UPDATE")
        return this
    }

    fun requireLogout(): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.REQUEST
        builder.putConfig("REQUIRE", "LOGOUT")
        return this
    }

    fun requireMessage(): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.REQUEST
        builder.putConfig("REQUIRE", "MESSAGE")
        return this
    }

    fun requireContacts(): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.REQUEST
        builder.putConfig("REQUIRE", "CONTACTS")
        return this
    }

    fun requireContact(des_uid: String): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.REQUEST
        builder.putConfig("REQUIRE", "CONTACT")
        builder.putPairs("des_uid", des_uid)
        return this
    }

    fun requireImage(names: List<String>): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.REQUEST
        builder.putConfig("REQUIRE", "IMAGE")
        names.forEach { builder.putPairs(it, " ") }
        return this
    }

    fun sendMessage(des_uid: String, data: UserMessageOuterClass.UserMessage): ProtocBuilder {
        builder.putConfig("DATATYPE", "UserMessage")
        builder.type = ProtocolOuterClass.ConnectType.TRANSIT
        builder.putPairs("des_uid", des_uid)
        putBytes("data", data.toByteString())
        return this
    }

    fun responseContact(): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.RESPONSE
        builder.putConfig("RESPONSE", "CONTACT")
        return this
    }

    fun responsePositive(): ProtocBuilder {
        builder.type = ProtocolOuterClass.ConnectType.RESPONSE
        return this
    }

}