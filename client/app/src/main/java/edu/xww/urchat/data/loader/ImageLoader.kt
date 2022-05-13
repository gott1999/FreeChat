package edu.xww.urchat.data.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import edu.xww.urchat.network.source.DataSource
import java.io.*
import java.util.concurrent.locks.ReentrantLock


class ImageLoader {

    /**
     * Try to find Image
     * @param context context
     * @param fileName file name
     * @return Bitmap? It will fail.
     */
    fun queryImage(context: Context, fileName: String): Bitmap? {
        if (TextUtils.isEmpty(fileName) || fileName == "") return null
        var bitmap: Bitmap? = null
        try {
            val picturesFile: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (picturesFile != null && picturesFile.exists() && picturesFile.isDirectory) {
                val files = picturesFile.listFiles()
                if (files != null) {
                    for (imageFile in files) {
                        val name = imageFile.name
                        Log.d("DiskLoader", "Check image $name")
                        if (imageFile.isFile && fileName == name) {
                            bitmap = BitmapFactory.decodeFile(imageFile.path)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("DiskLoader", "Load image error $fileName\n$e")
        }
        return bitmap
    }

}