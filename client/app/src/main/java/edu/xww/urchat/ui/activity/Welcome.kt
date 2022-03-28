package edu.xww.urchat.ui.activity

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import edu.xww.urchat.R
import edu.xww.urchat.data.MessageBox
import edu.xww.urchat.data.RunTimeData.RunTimeMessageBox

class Welcome : AppCompatActivity() {

    private var startCode = 0

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
        RunTimeMessageBox.pushAll(arrayOf(
            MessageBox("1", "TestMessage1","舔狗一号", "早！","05:20"),
            MessageBox("2", "TestMessage2","男神一号", "[图片]","12:01"),
            MessageBox("3", "TestMessage3","爹", "[图片]","14:01"),
            MessageBox("4", "TestMessage4","舔狗二号", "女神在吗？","刚刚"),
            MessageBox("5", "TestMessage5","舔狗三号", "姐姐在吗？","刚刚")
        ))
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