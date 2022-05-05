package edu.xww.urchat.data.runtime

import edu.xww.urchat.data.struct.user.Contract
import edu.xww.urchat.data.struct.user.ContractList
import edu.xww.urchat.data.struct.user.MessageBox
import edu.xww.urchat.data.struct.user.MessageStack
import java.util.*
import kotlin.collections.HashMap

object RunTimeData {

    var runTimeMessage = MessageStack()
        private set

    private var messageDataTimeMillis: Long = 0

    var runTimeContracts = ContractList()
        private set

    private var contractsDataTimeMillis: Long = 0

    val serverIp = Hashtable<String, String>()

    val serverPort = Hashtable<String, Int>()

    public fun clear() {
        serverIp.clear()
        serverPort.clear()
        runTimeMessage.clear()
        runTimeMessage = MessageStack()
        runTimeContracts.clear()
        messageDataTimeMillis = 0
        contractsDataTimeMillis = 0
    }

    public fun update() {
        test()

        pullContract()
        pullMessage()
        order()
    }

    public fun updateContract() {
        pullContract()
    }

    public fun updateMessage() {
        pullMessage()
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