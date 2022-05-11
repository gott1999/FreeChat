package edu.xww.urchat.data.loader

import edu.xww.urchat.network.source.DataSource

object LoaderManager {

    fun updateContact() {
        Thread {
            DataSource.contacts()
        }.start()
    }

    fun loadContact() {
        Thread {

        }.start()
    }

    fun updateMessage() {
        Thread {
            DataSource.message()
        }.start()
    }

    fun loadMessage() {
        Thread {

        }.start()
    }



}