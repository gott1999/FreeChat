package edu.xww.urchat.data.loader

import android.content.Context
import edu.xww.urchat.data.preserver.SResourcesPreserver
import edu.xww.urchat.network.source.DataSource

object SNetLoader {

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
        for (i in rt) SResourcesPreserver.forceSaveImage(context, i.key, i.value)
    }



}