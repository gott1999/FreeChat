package edu.xww.urchat.data.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.widget.ImageView
import java.lang.Exception
import java.lang.Float.min

object SImageLoader {

    val loader = ImageLoader()

    /**
     * Bind image from path
     */
    fun setImageView(context: Context, imageView: ImageView, path: String) {
        if (path == "") return
        Thread {
            try {
                var bitmap = loader.queryImage(context, path)

                if (bitmap == null) {
                    SNetLoader.pullImage(context, path)
                    bitmap = loader.queryImage(context, path)
                }

                if (bitmap != null) {
                    imageView.post { imageView.setImageBitmap(bitmap) }
                }
            } catch (e: Exception) {
                Log.d("ResourcesLoader", "Set image error: $e")
            }
        }.start()
    }

    /**
     * Bind image from path.
     * It can limit the max size
     */
    fun setImageView(
        context: Context,
        imgView: ImageView,
        path: String,
        maxWidth: Int,
        maxHeight: Int
    ) {
        Thread {
            try {
                var bitmap = loader.queryImage(context, path)

                if (bitmap == null) {
                    SNetLoader.pullImage(context, path)
                    bitmap = loader.queryImage(context, path)
                }

                if (bitmap != null) {
                    imgView.post { imgView.setImageBitmap(scale(bitmap, maxWidth, maxHeight)) }
                }

            } catch (e: Exception) {
                Log.d("ResourcesLoader", "Set image error: $e")
            }
        }.start()
    }

    /**
     * scale
     */
    private fun scale(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        var zoom = min(maxWidth.toFloat() / width, maxHeight.toFloat() / height)

        if (zoom > 1) zoom = 0.8f

        val matrix = Matrix()
        matrix.postScale(zoom, zoom)

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
    }

}