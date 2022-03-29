package edu.xww.urchat.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import edu.xww.urchat.R
import edu.xww.urchat.data.MessageBox
import edu.xww.urchat.data.RunTimeData
import java.lang.Exception

class ChatActivity : AppCompatActivity() {

    private val stk = RunTimeData.RunTimeMessageBox

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
    }

    private fun initData() {
        try {
            val s = intent.getSerializableExtra("messageId") as String
            messageBox = stk.getById(s)!!
        } catch (e : Exception) {
            e.printStackTrace()
            Toast.makeText(this, R.string._404, Toast.LENGTH_LONG).show()
        }
    }

    private fun showView() {
        if (messageBox.messageId == "-1") return

    }

}