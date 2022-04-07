package edu.xww.urchat.data.runtime

import edu.xww.urchat.data.struct.MessageBox
import edu.xww.urchat.data.struct.MessageStack
import java.util.*

object MessageData {

    private var lastUpdate: Date? = null

    var runTimeMessageBox = MessageStack()
        private set

    public fun clear() {
        lastUpdate = null
        runTimeMessageBox.clear()
        runTimeMessageBox = MessageStack()
    }

    public fun update() {
        updateTimestamp()
        updateMessage()
    }

    private fun updateTimestamp() {
        val now = Date(System.currentTimeMillis())
        lastUpdate = now
    }

    // TODO pull message
    private fun updateMessage() {
        setTestData()
    }

    private fun setTestData() {
        if (runTimeMessageBox.size() == 0) {
            for (i in 1..9) {
                runTimeMessageBox.push(
                    MessageBox(
                        "id $i",
                        "TestMessage1",
                        "舔狗$i 号",
                        "早安",
                        "05:0$i"
                    )
                )
            }
        }
    }
}