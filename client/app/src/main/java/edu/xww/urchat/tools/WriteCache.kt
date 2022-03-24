package edu.xww.urchat.tools

import android.os.Environment
import java.io.File

object WriteCache {

    /**
     * 检查 Cache文件夹
     */
    fun checkCaChe() {
        val cacheDir: File = Environment.getDownloadCacheDirectory()
        if (!cacheDir.exists()) cacheDir.mkdirs()
    }



}