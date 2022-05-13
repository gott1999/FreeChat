package edu.xww.urchat.data.loader

import edu.xww.urchat.network.source.DataSource
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object SConversationLoader {

    /**
     * One execute to manage "Message" and "Contact"
     */
    private val pool: ExecutorService = Executors.newFixedThreadPool(1)

    /**
     * Update Contact from server
     */
    fun updateContact() = pool.execute { DataSource.contacts() }

    /**
     * Load Contact from disk
     */
    fun loadContact() = pool.execute { }

    /**
     * Update "Message" from server
     */
    fun updateMessage() = pool.execute { DataSource.message() }

    /**
     * Load "Message" from disk
     */
    fun loadMessage() = pool.execute { }

}