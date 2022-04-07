package edu.xww.urchat.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import edu.xww.urchat.R
import edu.xww.urchat.data.struct.MessageBox
import edu.xww.urchat.data.runtime.MessageData
import java.lang.Exception

class ChatActivity : AppCompatActivity() {

    private var messageBox = MessageBox()

    companion object{
        fun startInstance(context: Context, messageId: String){
            val appContext = context.applicationContext
            val intent = Intent(appContext, ChatActivity::class.java)
            intent.putExtra("messageId", messageId)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            appContext.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initData()
        showView()

        // 返回
        val back = findViewById<ImageView>(R.id.activity_chat_head_back)
        back.setOnClickListener { this.finish() }

        // 菜单
        val menu = findViewById<ImageView>(R.id.activity_chat_head_menu)
        menu.setOnClickListener {  }


    }

    private fun initData() {
        try {
            val s = intent.getSerializableExtra("messageId") as String
            messageBox = MessageData.runTimeMessageBox.getById(s)!!
        } catch (e : Exception) {
            e.printStackTrace()
            Toast.makeText(this, R.string._404, Toast.LENGTH_LONG).show()
        }
    }

    private fun showView() {
        if (messageBox.messageId == "-1") return

        val textView = findViewById<TextView>(R.id.activity_chat_head_message_title)
        textView.text = messageBox.messageTitle

    }

}