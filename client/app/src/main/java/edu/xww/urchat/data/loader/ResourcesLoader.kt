package edu.xww.urchat.data.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.widget.ImageView
import edu.xww.urchat.ui.activity.ChatActivity
import edu.xww.urchat.ui.adapter.recyclerview.ChatMessageAdapter
import java.lang.Exception
import kotlin.math.min

object ResourcesLoader {

    val loader = DiskLoader()

    fun setImageBitmap(context: Context, imgView: ImageView, src: String) {
        if (src == "") {
            return
        }
        Log.d("ResourcesLoader", "Set Image Bitmap: $src")
        Thread {
            try {
                var bitmap = loader.queryImage(context, src)
                if (bitmap == null) {
                    Log.d("ResourcesLoader", "Try pull image: $src")
                    loader.pullImage(context, src)
                    bitmap = loader.queryImage(context, src)
                }
                if (bitmap != null) {
                    imgView.post {
                        imgView.setImageBitmap(bitmap)
                    }
                } else {
                    Log.d("ResourcesLoader", "Set image error: $src")
                }
            } catch (e: Exception) {
                Log.d("ResourcesLoader", "Set image error: $e")
            }
        }.start()
    }

    fun setBigImage(context: Context, imgView: ImageView, src: String) {
        Thread {
            try {
                var bitmap = loader.queryImage(context, src)
                if (bitmap == null) {
                    Log.d("ResourcesLoader", "Try pull image: $src")
                    loader.pullImage(context, src)
                    bitmap = loader.queryImage(context, src)
                }
                if (bitmap != null) {
                    val width = bitmap.width
                    val mw = ChatMessageAdapter.maxWidth
                    val zoom = mw.toFloat() / width

                    val matrix = Matrix()
                    matrix.postScale(zoom, zoom)
                    val nb = Bitmap.createBitmap(
                        bitmap,
                        0,
                        0,
                        bitmap.width,
                        bitmap.height,
                        matrix,
                        false
                    )

                    imgView.post {
                        imgView.setImageBitmap(nb)
                    }
                } else {
                    Log.d("ResourcesLoader", "Set image error: $src")
                }
            } catch (e: Exception) {
                Log.d("ResourcesLoader", "Set image error: $e")
            }
        }.start()
    }

    fun saveImage(context: Context, fileName: String, bitmap: Bitmap) {
        loader.createImage(context, fileName, bitmap)
    }


}