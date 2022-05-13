package edu.xww.urchat.network.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.protobuf.ByteString
import edu.xww.urchat.data.runtime.SRuntimeData
import edu.xww.urchat.data.runtime.SRuntimeData.SMessageList
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.data.struct.system.CommonRecyclerViewItem
import edu.xww.urchat.data.struct.user.Contact
import edu.xww.urchat.data.struct.user.Message
import edu.xww.urchat.network.builder.ProtocBuilder
import edu.xww.urchat.network.proto.ProtocolOuterClass
import edu.xww.urchat.network.proto.UserMessageOuterClass
import edu.xww.urchat.network.proto.UserMessageOuterClass.MessageType.*
import edu.xww.urchat.util.Encode
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object DataSource {

    /**
     * Request contact list
     */
    fun contacts() {
        val protoc = ProtocBuilder().requireContacts().buildValid()
        val rt = post(protoc)

        if (rt is Result.Success) {
            rt.data.bitsMap.forEach {
                try {
                    val uid = it.key

                    val runtimeContact = SRuntimeData.SContacts[uid]

                    val contact = ProtocolOuterClass.Protocol.parseFrom(it.value)

                    if (runtimeContact != null) {
                        runtimeContact.name =
                            contact.getPairsOrDefault("displayName", runtimeContact.name)
                        runtimeContact.icon = contact.getPairsOrDefault("icon", runtimeContact.icon)
                        runtimeContact.displayName =
                            contact.getPairsOrDefault("nickname", runtimeContact.displayName)
                    } else {
                        SRuntimeData.SContacts +=
                            Contact.buildNormal(
                                uid,
                                contact.getPairsOrDefault("displayName", ""),
                                contact.getPairsOrDefault("icon", ""),
                                contact.getPairsOrDefault(
                                    "nickname",
                                    contact.getPairsOrDefault("displayName", "Contact")
                                )
                            )
                    }
                } catch (e: Exception) {
                    Log.e("DataSource", "Load user contact error near ${it.key}\n$e")
                }
            }
        }
        SRuntimeData.SContacts.sort()
    }

    private val lock = ReentrantLock()

    /**
     * Request messages
     */
    fun message() {

        val protoc = ProtocBuilder().requireMessage().buildValid()
        val rt = post(protoc)
        if (rt is Result.Success) {
            rt.data.bitsMap.forEach {
                try {
                    val msg = UserMessageOuterClass.UserMessage.parseFrom(it.value)
                    var m: Message? = null
                    when (msg.type) {
                        TEXT -> m = Message.receiveNormalText(msg.message, msg.millisecondTimestamp)
                        IMAGE -> m = Message.receiveNormalImg(msg.message, msg.millisecondTimestamp)
                        POSITION -> m = Message.receiveNormalText(msg.message, msg.millisecondTimestamp)
                        ADD -> { SRuntimeData.SNewContacts += Contact.buildNormal(msg.src, "", "", msg.message, "new") }
                        DEL -> {}
                        else -> {}
                    }
                    if (m != null) {
                        SRuntimeData.SMessageBox[msg.src] = msg
                        SMessageList[msg.src] = m
                    }
                } catch (e: Exception) {
                    Log.e("DataSource", "Load user message error near ${it.key}\n$e")
                }
            }
        }
    }

    /**
     * Request image
     */
    fun image(names: MutableList<String>): HashMap<String, Bitmap> {
        val protoc = ProtocBuilder().requireImage(names).buildValid()
        val rt = post(protoc)
        val map: HashMap<String, Bitmap> = HashMap()
        if (rt is Result.Success) {
            rt.data.bitsMap.forEach { map[it.key] = Encode.toBitmap(it.value) }
        }
        return map
    }

    /**
     * post protoc
     */
    private fun post(protoc: ProtocolOuterClass.Protocol): Result<ProtocolOuterClass.Protocol> {
        return try {
            val rt =
                SocketHelper.sendRequest(protoc) ?: return Result.Error("Can't connect to server")

            if (!rt.valid) return Result.Error("Post Fail")
            Result.Success(rt)
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }

    /**
     * send protoc
     */
    fun send(protoc: ProtocolOuterClass.Protocol): Result<String> {
        if (post(protoc) is Result.Success) return Result.Success("")
        return Result.Error("Post Fail")
    }

    /**
     * send UserMessageOuterClass.UserMessage
     */
    fun sendMessage(msg: UserMessageOuterClass.UserMessage): Result<String> {
        val protoc = ProtocBuilder().sendMessage(msg.des, msg).buildValid()
        if (post(protoc) is Result.Success) return Result.Success("")
        return Result.Error("Post Fail")
    }

    /**
     * update icon
     */
    fun updateIcon(filename: String, bitmap: Bitmap): Result<String> {
        val s = Encode.toBase64(bitmap)
        val protoc =
            ProtocBuilder().requireUpdate().putPairs("icon", filename).putBytes(filename, s)
                .buildValid()

        if (post(protoc) is Result.Success) return Result.Success("")
        return Result.Error("Post Fail")
    }

    /**
     * response contact application
     */
    fun responseContact(des: String): Result<String> {
        val protoc = ProtocBuilder().putPairs("des_uid", des).responseContact().buildValid()
        if (post(protoc) is Result.Success) return Result.Success("")
        return Result.Error("Post Fail")
    }

    /**
     * send image
     */
    fun sendImage(msg: UserMessageOuterClass.UserMessage, bitmap: Bitmap): Result<String> {
        val s = Encode.toBase64(bitmap)

        val protoc = ProtocBuilder().sendMessage(msg.des, msg).putBytes(msg.message, s).buildValid()

        if (post(protoc) is Result.Success) return Result.Success("")
        return Result.Error("Post Fail")
    }

}