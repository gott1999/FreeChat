package edu.xww.urchat.data.preserver

import android.content.Context
import android.graphics.Bitmap

object SResourcesPreserver {

    private val imagePreserver = ImagePreserver()

    fun saveImage(context: Context, fileName: String, bitmap: Bitmap) {
        imagePreserver.createImage(context, fileName, bitmap, false)
    }

    fun forceSaveImage(context: Context, fileName: String, bitmap: Bitmap) {
        imagePreserver.createImage(context, fileName, bitmap, true)
    }


}