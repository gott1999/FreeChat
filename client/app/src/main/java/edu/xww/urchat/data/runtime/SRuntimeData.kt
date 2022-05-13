package edu.xww.urchat.data.runtime

import edu.xww.urchat.data.struct.system.CommonRecyclerViewItem
import edu.xww.urchat.data.struct.user.ContactList
import edu.xww.urchat.data.struct.user.Message
import edu.xww.urchat.data.struct.user.MessageList
import edu.xww.urchat.data.struct.user.MessageStack
import java.util.*
import kotlin.collections.HashMap

/**
 * Saving runtime data
 */
object SRuntimeData {

    /**
     * Saving message box for "MessageFragment"
     */
    var SMessageBox = MessageStack()
        private set

    /**
     * Saving message box for "ContactFragment"
     */
    var SContacts = ContactList()
        private set

    /**
     * Saving new contacts for "NewFriendActivity"
     */
    var SNewContacts = arrayListOf<CommonRecyclerViewItem>()
        private set

    /**
     * Saving new contacts for every "ChatActivity"
     */
    var SMessageList = MessageList()
        private set

    /**
     * Server IP
     * "main" is the main server
     */
    val SIP = Hashtable<String, String>()

    /**
     * Server PORT
     * "main" is the main port
     */
    val SPort = Hashtable<String, Int>()

    /**
     * clear message
     */
    fun clear() {
        SIP.clear()
        SPort.clear()

        // Since circular references occur, you need to manually create a new stack.
        SMessageBox.clear()
        SMessageBox = MessageStack()

        SContacts.clear()

        SNewContacts.clear()
        SMessageList.clear()
    }

}