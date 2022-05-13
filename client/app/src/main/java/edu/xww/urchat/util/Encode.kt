package edu.xww.urchat.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.protobuf.ByteString
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*


object Encode {

    fun toBase64(bitmap: Bitmap): ByteString {
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        val res = output.toByteArray()

        val b = Base64.getEncoder().encodeToString(res)
        return ByteString.copyFromUtf8(b)
    }

    fun toBitmap(byteString: ByteString): Bitmap {
        val data = byteString.toStringUtf8()
        val bit = Base64.getDecoder().decode(data)
        return BitmapFactory.decodeByteArray(bit, 0, bit.size)
    }

    fun hash(inputs: String, algorithm: String = "SHA-256"): String {
        val res = StringBuilder()
        try {
            val instance = MessageDigest.getInstance(algorithm)
            val newBytes = instance.digest(inputs.toByteArray(StandardCharsets.UTF_8))
            var temp: String
            for (b in newBytes) {
                temp = Integer.toHexString(b.toInt() and 0xff)
                if (temp.length == 1) res.append('0')
                res.append(temp)
            }
        } catch (e: Exception) {
            res.clear()
            e.printStackTrace()
        }
        return res.toString()
    }

}