package edu.xww.urchat.util

import java.lang.Exception
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


object Encode {

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