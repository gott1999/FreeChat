package edu.xww.urchat.ui.activity

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import edu.xww.urchat.R
import edu.xww.urchat.data.MessageBox
import edu.xww.urchat.data.RunTimeData
import java.util.*

class Welcome : AppCompatActivity() {

    private var startCode = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        update()
        launch()
    }

    /**
     * 更新数据
     */
    private fun update() {
        val now = Date(System.currentTimeMillis())

        if (RunTimeData.RunTimeMessageBox.size() == 0) {
            for (i in 1..9) {
                RunTimeData.RunTimeMessageBox.push(MessageBox(
                    "id $i",
                    "TestMessage1",
                    "舔狗$i 号",
                    "早安",
                    "05:0$i"))
            }
            RunTimeData.lastUpdate = now
        }
        startCode = 0
    }

    /**
     * 跳转Main
     */
    private fun launch() {
        if (startCode == 0) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            // TODO 启动失败
        }
    }

}