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


class DiskLoader {

    private val lock = ReentrantLock()

    fun createImage(context: Context, fileName: String, bitmap: Bitmap) {
        while (lock.isLocked)
        lock.lock()
        try {
            Log.d("DiskLoader/CreateImage", "try to create $fileName")
            val picture : File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (picture != null && !picture.exists()) {
                picture.mkdir()
            }

            val imageFile = File("$picture/$fileName")

            if (imageFile.exists()) {
                Log.d("DiskLoader/CreateImage", "$picture/$fileName is already exited")
                imageFile.delete()
            }
            Log.d("DiskLoader/CreateImage", "try to saving $picture/$fileName")
            val fileOutputStream = FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            Log.d("DiskLoader/CreateImage", "Saving $picture/$fileName Success")
        } catch (e: IOException) {
            Log.d("DiskLoader/CreateImage", "/$fileName saving fail")
            e.printStackTrace()
        } finally {
            if (lock.isLocked) lock.unlock()
        }
    }

    fun queryImage(context: Context, fileName: String): Bitmap? {
        if (TextUtils.isEmpty(fileName)) return null
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

    fun pullImage(context: Context, fileName: String) {
        val rt = DataSource.image(arrayListOf(fileName))
        for (i in rt) {
            Log.d("DiskLoader", "Pulled image ${i.key}, size ${i.value.width}*${i.value.height}")
            createImage(context, i.key, i.value)
        }
    }





}