package edu.xww.urchat.data.preserver

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import edu.xww.urchat.network.source.DataSource
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.locks.ReentrantLock

class ImagePreserver {

    /**
     * A reentrant lock.
     */
    private val lock = ReentrantLock()

    /**
     * Create Image
     * @param context context
     * @param fileName file name
     * @param bitmap Image bitmap
     * @param force If file is existed force to replace
     */
    fun createImage(context: Context, fileName: String, bitmap: Bitmap, force: Boolean) {
        synchronized(lock) {
            try {

                val picture : File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

                if (picture != null && !picture.exists()) picture.mkdir()

                val imageFile = File("$picture/$fileName")

                if (imageFile.exists() && force) imageFile.delete()

                val fileOutputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
            } catch (e: IOException) {
                Log.d("DiskLoader/CreateImage", "/$fileName saving fail $e")
            }
        }
    }

    /**
     * Try to pull image from Server, will save to the disk.
     * Server File will replace the local.
     * @param context context
     * @param fileName file name
     */
    fun pullImage(context: Context, fileName: String) {
        pullImages(context, arrayListOf(fileName))
    }

    /**
     * Try to pull image from Server, will save to the disk.
     * Server File will replace the local.
     * @param context context
     * @param fileNames files
     */
    fun pullImages(context: Context, fileNames: MutableList<String>) {
        val rt = DataSource.image(fileNames)
        for (i in rt) createImage(context, i.key, i.value, true)
    }


}