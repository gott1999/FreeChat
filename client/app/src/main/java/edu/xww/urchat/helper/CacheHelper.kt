package edu.xww.urchat.helper

import android.os.Environment
import java.io.File

object CacheHelper {

    /**
     * 检查 Cache文件夹
     */
    fun checkCaChe() {
        val cacheDir: File = Environment.getDownloadCacheDirectory()
        if (!cacheDir.exists()) cacheDir.mkdirs()
    }


}