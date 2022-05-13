package edu.xww.urchat.data.struct.user

import java.util.ArrayList
import java.util.HashMap

class ContactList {

    private val contracts: MutableList<Contact> = ArrayList()

    private val uidToContract: MutableMap<String, Contact> = HashMap()

    var size = 0
        private set

    fun clear() {
        size = 0
        contracts.clear()
        uidToContract.clear()
    }

    operator fun get(index: Int): Contact {
        return contracts[index]
    }

    operator fun get(uid: String): Contact? {
        return uidToContract[uid]
    }

    operator fun plusAssign (contract: Contact) {
        contracts.add(contract)
        uidToContract[contract.uid] = contract
        ++size
    }

    operator fun minusAssign (index: Int) {
        val (m_uId) = contracts[index]
        uidToContract.remove(m_uId)
        contracts.removeAt(index)
        --size
    }

    fun sort() {
        contracts.sortBy { it.displayName }
    }
}