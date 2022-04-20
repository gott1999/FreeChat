package edu.xww.urchat.data.struct.user

import java.util.ArrayList
import java.util.HashMap

class ContractList {

    private val contracts: MutableList<Contract> = ArrayList()

    private val uidToContract: MutableMap<String, Contract> = HashMap()

    var size = 0
        private set


    public fun clear() {
        size = 0
        contracts.clear()
        uidToContract.clear()
    }

    public operator fun get(index: Int): Contract {
        return contracts[index]
    }

    public operator fun get(uid: String): Contract? {
        return uidToContract[uid]
    }

    public fun add(contract: Contract) {
        contracts.add(contract)
        uidToContract[contract.uId] = contract
        ++size
    }

    public fun remove(index: Int) {
        val (m_uId) = contracts[index]
        uidToContract.remove(m_uId)
        contracts.removeAt(index)
        --size
    }

    public fun sort() {
        contracts.sortBy { it.displayName }
    }
}