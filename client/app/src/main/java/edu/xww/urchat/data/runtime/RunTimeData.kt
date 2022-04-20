package edu.xww.urchat.data.runtime

import edu.xww.urchat.data.struct.user.Contract
import edu.xww.urchat.data.struct.user.ContractList
import edu.xww.urchat.data.struct.user.MessageBox
import edu.xww.urchat.data.struct.user.MessageStack
import java.util.*

object RunTimeData {

    private var lastUpdate: Date? = null

    var runTimeMessage = MessageStack()
        private set

    var runTimeContracts = ContractList()
        private set


    public fun clear() {
        runTimeMessage.clear()
        runTimeMessage = MessageStack()
        runTimeContracts.clear()
        lastUpdate = null
    }

    public fun update() {
        test()

        updateTimestamp()
        pullContract()
        pullMessage()
        order()
    }

    public fun updateContract() {
        updateTimestamp()
        pullContract()
    }

    public fun updateMessage() {
        updateTimestamp()
        pullMessage()
    }

    private fun updateTimestamp() {
        val now = Date(System.currentTimeMillis())
        lastUpdate = now
    }

    // TODO pull Contract
    private fun pullContract() {


    }

    // TODO pull Message
    private fun pullMessage() {

    }


    private fun order() {
        runTimeContracts.sort()
    }

    private fun test() {
        for (i in 1..9) {
            runTimeContracts.add(
                Contract.buildNormal(
                    "10000000$i",
                    "ffx$i",
                    "123123",
                    "fff$i",
                    "#"
                )
            )
            runTimeMessage.push(
                MessageBox(
                    "$i",
                    "TestMessage1",
                    "舔狗$i 号",
                    "早安",
                    "05:0$i"
                )
            )
        }
    }

}