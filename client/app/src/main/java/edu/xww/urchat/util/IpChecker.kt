package edu.xww.urchat.util

import java.lang.Exception

object IpChecker {

    fun checkIpv4(string: String): Boolean {
        if (string.count { it == '.' } != 3) return false
        val ipList = string.split('.')
        if (ipList.count() != 4) return false

        try {
            if (ipList[0].toInt() < 1 || ipList[0].toInt() > 255) return false
        } catch (e: Exception) {
            return false
        }

        for (i in 1..3) {

            try {
                val n = ipList[i].toInt()
                if (n < 0 || n > 255) return false
            } catch (e: Exception) {
                return false
            }
        }

        return true
    }


}