package edu.xww.urchat.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R
import edu.xww.urchat.adapter.recyclerview.ChatMessageAdapter
import edu.xww.urchat.data.struct.user.Message
import edu.xww.urchat.data.struct.user.Message.Companion.sendNormalText
import java.lang.Exception

class ChatActivity : AppCompatActivity(), View.OnClickListener {

    private var messageId = ""

    private var displayTitle = ""

    private val messageList = ArrayList<Message>()

    private lateinit var recyclerView: RecyclerView

    /**
     * While
     * type == 0 means default.
     * type == 1 means send message.
     */
    private var functionKeyType = 0

    companion object {
        /**
         * Use startInstance to start this instance
         */
        fun startInstance(context: Context, messageId: String, displayName: String) {
            val appContext = context.applicationContext
            val intent = Intent(appContext, ChatActivity::class.java)
            intent.putExtra("messageId", messageId)
            intent.putExtra("displayName", displayName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            appContext.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initData()
        bindViews()
        bindAdapter()
    }


    private fun initData() {
        try {
            messageId = intent.getSerializableExtra("messageId") as String
            displayTitle = intent.getSerializableExtra("displayName") as String
            // TODO get data
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, R.string._404, Toast.LENGTH_LONG).show()
        }
    }

    private fun bindViews() {
        // back icon
        val back: ImageView = findViewById(R.id.activity_chat_head_back)
        back.setOnClickListener(this)

        // menu icon
        val menu: ImageView = findViewById(R.id.activity_chat_head_menu)
        menu.setOnClickListener(this)

        // title
        val title: TextView = findViewById(R.id.activity_chat_head_message_title)
        title.text = displayTitle
        title.setOnClickListener(this)

        // video_call icon
        val call: ImageView = findViewById(R.id.activity_chat_foot_video_call)
        call.setOnClickListener(this)

        // emoji icon
        val emoji: ImageView = findViewById(R.id.activity_chat_foot_emoji_emotions)
        emoji.setOnClickListener(this)

        // function_key icon
        val functionKey: ImageView = findViewById(R.id.activity_chat_foot_function_keys)
        functionKey.setOnClickListener(this)

        // message witch user inputs
        val editText: EditText = findViewById(R.id.activity_chat_foot_input)
        editText.doAfterTextChanged { onTextChanged(it.toString().length, functionKey) }
    }

    private fun onTextChanged(count: Int, functionKey: ImageView) {
        functionKeyType = if (count > 0) {
            functionKey.setImageResource(R.drawable.ic_outline_send_24)
            1
        } else {
            functionKey.setImageResource(R.drawable.ic_outline_add_circle_outline_24)
            0
        }
    }

    private fun bindAdapter() {
        recyclerView = findViewById(R.id.activity_chat_message_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChatMessageAdapter(messageList)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // activity_chat_head
            R.id.activity_chat_head_back -> this.finish()
            R.id.activity_chat_head_menu -> onMenuClicked()
            R.id.activity_chat_head_message_title -> onTitleClicked()
            // activity_chat_foot
            R.id.activity_chat_foot_video_call -> onVideoCallClicked()
            R.id.activity_chat_foot_emoji_emotions -> onEmojiClicked()
            R.id.activity_chat_foot_function_keys -> onFunctionKeyClicked()
            // others
            else -> Toast.makeText(this, R.string.NotFinished, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onMenuClicked() {
        Toast.makeText(this, R.string.NotFinished, Toast.LENGTH_SHORT).show()
    }

    private fun onTitleClicked() {
        Toast.makeText(this, R.string.NotFinished, Toast.LENGTH_SHORT).show()
    }

    private fun onVideoCallClicked() {
        Toast.makeText(this, R.string.NotFinished, Toast.LENGTH_SHORT).show()
    }

    private fun onEmojiClicked() {
        Toast.makeText(this, R.string.NotFinished, Toast.LENGTH_SHORT).show()
    }

    private fun onFunctionKeyClicked() {
        when (functionKeyType) {
            0 -> showFunctions()
            1 -> sendMessage()
        }
    }

    private fun showFunctions() {
        // TODO show functions
        Toast.makeText(this, R.string.NotFinished, Toast.LENGTH_SHORT).show()
    }

    private fun sendMessage() {
        val editText = findViewById<EditText>(R.id.activity_chat_foot_input)
        val text = editText.text.toString()
        if (text.isNotEmpty()) {
            val message = sendNormalText(text)
            messageList.add(message)
            recyclerView.adapter?.notifyItemChanged(messageList.size - 1)
            // TODO Send message
            try {

                // success
                editText.setText("")
            } catch (e: Exception) {
                Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show()
            }
        }
        // scroll to bottom
        recyclerView.scrollToPosition(messageList.size - 1)
    }
}