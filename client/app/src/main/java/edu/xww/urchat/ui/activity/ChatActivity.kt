package edu.xww.urchat.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.xww.urchat.R
import edu.xww.urchat.data.loader.ResourcesLoader
import edu.xww.urchat.data.runtime.RunTimeData
import edu.xww.urchat.data.runtime.RunTimeData.runTimeMessageList
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.data.struct.user.Message
import edu.xww.urchat.data.struct.user.Message.Companion.sendNormalImg
import edu.xww.urchat.data.struct.user.Message.Companion.sendNormalText
import edu.xww.urchat.network.builder.MessageBuilder
import edu.xww.urchat.network.source.DataSource
import edu.xww.urchat.ui.adapter.recyclerview.ChatMessageAdapter


class ChatActivity : AppCompatActivity(), View.OnClickListener {

    private var tarUid = ""

    private var displayTitle = ""

    private var messageList: ArrayList<Message>? = null

    private lateinit var recyclerView: RecyclerView

    private val select = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            Log.d("ChatActivity/select", "get picture ${it.path}")

            val filename = "${it.hashCode()}.png"

            val photoBmp = MediaStore.Images.Media.getBitmap(contentResolver, it)
            ResourcesLoader.saveImage(this, filename, photoBmp)
            val message = sendNormalImg(filename)
            messageList!!.add(message)
            recyclerView.adapter?.notifyItemChanged(messageList!!.size - 1)

            try {
                Thread {
                    val m = MessageBuilder()
                        .setDesUid(tarUid)
                        .setMessage(filename)
                        .setType(MessageBuilder.Type.IMAGE)
                        .build()

                    val res = DataSource.sendImg(m, photoBmp)

                    if (res is Result.Success) {
                        // success
                        Log.d("ChatActivity", "Say to '$tarUid': '$filename'")
                        RunTimeData.runTimeMessage.push(
                            tarUid,
                            "${this.getString(R.string.you)}: ${this.getString(R.string.picture)}",
                            System.currentTimeMillis()
                        )
                    } else {
                        this.runOnUiThread {
                            Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }


    }

    /**
     * While
     * type == 0 means default.
     * type == 1 means send message.
     */
    private var functionKeyType = 0

    private lateinit var popupMenu:PopupMenu

    companion object {
        /**
         * Use startInstance to start this instance
         */
        fun startInstance(context: Context, messageId: String) {
            val appContext = context.applicationContext
            val intent = Intent(appContext, ChatActivity::class.java)
            intent.putExtra("tarUid", messageId)
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
            tarUid = intent.getSerializableExtra("tarUid") as String
            displayTitle = RunTimeData.runTimeContacts[tarUid]!!.displayName

            if (!runTimeMessageList.containsKey(tarUid))
                runTimeMessageList[tarUid] = ArrayList()
            messageList = runTimeMessageList[tarUid]

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
        val popupMenu = PopupMenu(this, menu)
        popupMenu.menuInflater.inflate(R.menu.menu_chat, popupMenu.menu);
        popupMenu.setOnMenuItemClickListener{

            return@setOnMenuItemClickListener false
        }
        popupMenu.setOnDismissListener {

        }
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
        recyclerView.adapter = ChatMessageAdapter(this, tarUid)

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
        popupMenu.show();
    }

    private fun onTitleClicked() {
        Toast.makeText(this, R.string.NotFinished, Toast.LENGTH_SHORT).show()
    }

    private fun onVideoCallClicked() {
        Toast.makeText(this, R.string.NotFinished, Toast.LENGTH_SHORT).show()
    }

    private fun onEmojiClicked() {

        val p = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val checkWrite = ContextCompat.checkSelfPermission(this, p[0])
        val checkRead = ContextCompat.checkSelfPermission(this, p[1])
        val ok = PackageManager.PERMISSION_GRANTED

        if (checkWrite != ok && checkRead != ok) {
            ActivityCompat.requestPermissions(this, p, 1)
        } else {
            select.launch("image/*")
        }

    }

    private fun onFunctionKeyClicked() {
        when (functionKeyType) {
            0 -> showFunctions()
            1 -> sendMessage()
        }
    }

    private fun showFunctions() {

        Toast.makeText(this, R.string.NotFinished, Toast.LENGTH_SHORT).show()
    }

    private fun sendMessage() {
        val editText = findViewById<EditText>(R.id.activity_chat_foot_input)
        val text = editText.text.toString()
        if (text.isNotEmpty()) {
            val message = sendNormalText(text)
            messageList!!.add(message)
            recyclerView.adapter?.notifyItemChanged(messageList!!.size - 1)
            editText.setText("")
            try {
                Thread {

                    val m = MessageBuilder()
                        .setDesUid(tarUid)
                        .setMessage(text)
                        .setType(MessageBuilder.Type.TEXT)
                        .build()
                    val res = DataSource.sendMessage(m)

                    if (res is Result.Success) {
                        // success
                        Log.d("ChatActivity", "Say to '$tarUid': '$text'")
                        RunTimeData.runTimeMessage.push(
                            tarUid,
                            "${this.getString(R.string.you)}:$text",
                            System.currentTimeMillis()
                        )
                    } else {
                        this.runOnUiThread {
                            Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()


            } catch (e: Exception) {
                Log.d("Send Message", e.toString())
                Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show()
            }
        }
        // scroll to bottom
        recyclerView.scrollToPosition(messageList!!.size - 1)
    }


}