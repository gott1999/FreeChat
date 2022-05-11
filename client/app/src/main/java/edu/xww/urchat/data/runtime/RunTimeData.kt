package edu.xww.urchat.data.runtime

import edu.xww.urchat.data.struct.user.ContactList
import edu.xww.urchat.data.struct.user.Message
import edu.xww.urchat.data.struct.user.MessageStack
import java.util.*
import kotlin.collections.HashMap

object RunTimeData {

    var runTimeMessage = MessageStack()
        private set

    var runTimeContacts = ContactList()
        private set

    var runTimeMessageList = HashMap<String, ArrayList<Message>>()

    /**
     * server ip pool
     * "main" is the main server
     */
    val serverIp = Hashtable<String, String>()

    /**
     * server ip pool
     * "main" is the main port
     */
    val serverPort = Hashtable<String, Int>()

    /**
     * clear message
     */
    fun clear() {
        serverIp.clear()
        serverPort.clear()

        // Since circular references occur, you need to manually create a new stack.
        runTimeMessage.clear()
        runTimeMessage = MessageStack()

        runTimeContacts.clear()
    }

}