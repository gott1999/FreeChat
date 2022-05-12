package edu.xww.urchat.network.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Debug
import android.util.Log
import com.google.protobuf.ByteString
import edu.xww.urchat.data.runtime.RunTimeData
import edu.xww.urchat.data.runtime.RunTimeData.runTimeMessageList
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.data.struct.system.CommonRecyclerViewItem
import edu.xww.urchat.data.struct.user.Contact
import edu.xww.urchat.data.struct.user.Message
import edu.xww.urchat.network.builder.ProtocBuilder
import edu.xww.urchat.network.proto.ProtocolOuterClass
import edu.xww.urchat.network.proto.UserMessageOuterClass
import edu.xww.urchat.network.proto.UserMessageOuterClass.MessageType.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object DataSource {

    fun contacts() {
        val protoc = ProtocBuilder().requireContacts().buildValid()
        val rt = post(protoc)
        if (rt is Result.Success) {
            rt.data.bitsMap.forEach {
                try {
                    val uid = it.key
                    val runtimeContact = RunTimeData.runTimeContacts[uid]
                    val contact = ProtocolOuterClass.Protocol.parseFrom(it.value)
                    if (runtimeContact != null) {
                        val nickname = contact.getPairsOrDefault("nickname", "")
                        val icon = contact.getPairsOrDefault("icon", "")
                        val displayName = contact.getPairsOrDefault("displayName", "")
                        runtimeContact.name = displayName!!
                        runtimeContact.icon = icon!!
                        runtimeContact.displayName = nickname!!
                    } else {
                        RunTimeData.runTimeContacts.add(
                            Contact.buildNormal(
                                uid,
                                contact.getPairsOrDefault("displayName", ""),
                                contact.getPairsOrDefault("icon", ""),
                                contact.getPairsOrDefault(
                                    "nickname",
                                    contact.getPairsOrDefault("displayName", "Contact")
                                ),
                                "#"
                            )
                        )
                    }
                } catch (e: Exception) {
                    Log.e("DataSource", "Load user contact error near ${it.key}\n$e")
                }
            }
        }
        RunTimeData.runTimeContacts.sort()
    }

    fun message() {
        val protoc = ProtocBuilder().requireMessage().buildValid()
        val rt = post(protoc)
        if (rt is Result.Success) {
            rt.data.bitsMap.forEach { entry ->
                try {
                    val msg = UserMessageOuterClass.UserMessage.parseFrom(entry.value)
                    Log.d("DataSource/message", msg.message)
                    if (!runTimeMessageList.containsKey(msg.src)) {
                        runTimeMessageList[msg.src] = ArrayList()
                    }
                    var m: Message? = null
                    when (msg.type) {
                        TEXT -> m = Message.receiveNormalText(msg.message)
                        IMAGE -> m = Message.receiveNormalImg(msg.message)
                        POSITION -> m = Message.receiveNormalText(msg.message)
                        ADD -> {
                            RunTimeData.newContacts.add(
                                CommonRecyclerViewItem(
                                    "",
                                    msg.message,
                                    "new",
                                    msg.src
                                )
                            )
                        }
                        DEL -> {}
                        else -> {}
                    }
                    if (m != null) {
                        m.time = msg.millisecondTimestamp
                        RunTimeData.runTimeMessage.push(
                            msg.src,
                            msg.message,
                            msg.millisecondTimestamp
                        )
                        runTimeMessageList[msg.src]!!.add(m)
                        runTimeMessageList[msg.src]?.sortBy { it.time }
                    }
                } catch (e: Exception) {
                    Log.e("DataSource", "Load user message error near ${entry.key}\n$e")
                }
            }
        }
    }

    fun image(names: MutableList<String>): HashMap<String, Bitmap> {
        val protoc = ProtocBuilder().requireImage(names).buildValid()
        val rt = post(protoc)
        val map: HashMap<String, Bitmap> = HashMap()
        if (rt is Result.Success) {
            rt.data.bitsMap.forEach {
                Log.d("DataSource/image", "receive ${it.key}")
                val data = it.value.toStringUtf8()
                val bit = Base64.getDecoder().decode(data)

                val bitmap = BitmapFactory.decodeByteArray(bit, 0, bit.size)
                map[it.key] = bitmap
            }
        }
        return map
    }

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

    fun sendMessage(msg: UserMessageOuterClass.UserMessage): Result<String> {
        val protoc = ProtocBuilder().sendMessage(msg.des, msg).buildValid()
        val rt = post(protoc)
        if (rt is Result.Success)
            return Result.Success("")
        return Result.Error("Post Fail")
    }

    fun send(protoc: ProtocolOuterClass.Protocol): Result<String> {
        val rt = post(protoc)
        if (rt is Result.Success)
            return Result.Success("")
        return Result.Error("Post Fail")
    }

    fun updateIcon(filename: String, bitmap: Bitmap): Result<String> {
        val s = toBase64(bitmap)
        Log.d("DataSource", "user try update icon")
        val protoc = ProtocBuilder()
            .requireUpdate()
            .putPairs("icon", filename)
            .putBytes(filename, s)
            .buildValid()

        val rt = post(protoc)
        if (rt is Result.Success)
            return Result.Success("")
        return Result.Error("Post Fail")
    }

    fun responseContact(des: String): Result<String> {
        val protoc = ProtocBuilder().putPairs("des_uid", des).responseContact().buildValid()
        val rt = post(protoc)
        if (rt is Result.Success)
            return Result.Success("")
        return Result.Error("Post Fail")
    }

    fun sendImg(msg: UserMessageOuterClass.UserMessage, bitmap: Bitmap): Result<String> {
        val s = toBase64(bitmap)

        val protoc = ProtocBuilder()
            .sendMessage(msg.des, msg)
            .putBytes(msg.message, s)
            .buildValid()

        val rt = post(protoc)
        if (rt is Result.Success)
            return Result.Success("")
        return Result.Error("Post Fail")
    }


    private fun toBase64(bitmap: Bitmap): ByteString {
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        val res = output.toByteArray()

        val b = Base64.getEncoder().encodeToString(res)
        return ByteString.copyFromUtf8(b)
    }

}