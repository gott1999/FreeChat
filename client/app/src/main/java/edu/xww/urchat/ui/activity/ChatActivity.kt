package edu.xww.urchat.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
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
import edu.xww.urchat.data.preserver.SResourcesPreserver
import edu.xww.urchat.data.runtime.SRuntimeData
import edu.xww.urchat.data.runtime.SRuntimeData.SMessageList
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.data.struct.user.Message
import edu.xww.urchat.data.struct.user.Message.Companion.sendNormalImg
import edu.xww.urchat.data.struct.user.Message.Companion.sendNormalText
import edu.xww.urchat.network.builder.MessageBuilder
import edu.xww.urchat.network.source.DataSource
import edu.xww.urchat.ui.adapter.recyclerview.ChatMessageAdapter


class ChatActivity : AppCompatActivity(), View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    /**
     * tarUid
     */
    private lateinit var tarUid: String

    /**
     * title message
     */
    private lateinit var title: String

    /**
     * MessageList cache
     */
    private lateinit var messageList: ArrayList<Message>

    /**
     * recyclerView
     */
    private lateinit var recyclerView: RecyclerView

    /**
     * PopupMenu of menu
     */
    private lateinit var menuMenu: PopupMenu

    /**
     * PopupMenu of function
     */
    private lateinit var functionMenu: PopupMenu

    /**
     * Image selecter
     */
    private val select = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            val filename = "${it.hashCode()}"

            val photoBmp = MediaStore.Images.Media.getBitmap(contentResolver, it)

            SResourcesPreserver.saveImage(this, filename, photoBmp)

            messageList.add(sendNormalImg(filename, System.currentTimeMillis()))

            recyclerView.adapter?.notifyItemChanged(messageList.size - 1)

            try {
                Thread {
                    val m = MessageBuilder()
                        .setDesUid(tarUid)
                        .setMessage(filename)
                        .setType(MessageBuilder.Type.IMAGE)
                        .build()

                    val res = DataSource.sendImage(m, photoBmp)

                    if (res is Result.Success) {
                        SRuntimeData.SMessageBox[tarUid] =
                            "${this.getString(R.string.you)}: ${this.getString(R.string.picture)}"
                    } else {
                        this.runOnUiThread {
                            Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()
            } catch (e: java.lang.Exception) {
                Log.e("ChatActivity/select/Thread", "$e")
            }
        }
    }

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

    /**
     * init tar_uid title messageList
     */
    private fun initData() {
        tarUid = intent.getSerializableExtra("tarUid") as String
        title = SRuntimeData.SContacts[tarUid]!!.displayName
        messageList = SMessageList[tarUid]
    }

    /**
     * bind views
     */
    private fun bindViews() {
        // back icon
        val back: ImageView = findViewById(R.id.activity_chat_head_back)
        back.setOnClickListener(this)

        // menu icon
        val menu: ImageView = findViewById(R.id.activity_chat_head_menu)
        menu.setOnClickListener(this)
        menuMenu = PopupMenu(this, menu)
        menuMenu.menuInflater.inflate(R.menu.menu_chat, menuMenu.menu)
        menuMenu.setOnMenuItemClickListener(this)

        // title
        val tv: TextView = findViewById(R.id.activity_chat_head_message_title)
        tv.text = title

        // video_call icon
        val call: ImageView = findViewById(R.id.activity_chat_foot_video_call)
        call.setOnClickListener(this)

        // emoji icon
        val emoji: ImageView = findViewById(R.id.activity_chat_foot_emoji_emotions)
        emoji.setOnClickListener(this)

        // function_key icon
        val functionKey: ImageView = findViewById(R.id.activity_chat_foot_function_keys)
        functionKey.setOnClickListener(this)
        functionMenu = PopupMenu(this, functionKey)
        functionMenu.menuInflater.inflate(R.menu.munu_function, functionMenu.menu)
        functionMenu.setOnMenuItemClickListener(this)

        // message witch user inputs
        val editText: EditText = findViewById(R.id.activity_chat_foot_input)
        editText.doAfterTextChanged { onTextChanged(it.toString().length, functionKey) }
    }

    /**
     * Input edit view change
     */
    private fun onTextChanged(count: Int, functionKey: ImageView) {
        functionKeyType = if (count > 0) {
            functionKey.setImageResource(R.drawable.ic_outline_send_24)
            1
        } else {
            functionKey.setImageResource(R.drawable.ic_outline_add_circle_outline_24)
            0
        }
    }

    /**
     * bind recyclerView
     */
    private fun bindAdapter() {
        recyclerView = findViewById(R.id.activity_chat_message_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChatMessageAdapter(this, tarUid)
    }

    /**
     * onclick
     */
    override fun onClick(view: View?) {
        when (view?.id) {
            // activity_chat_head
            R.id.activity_chat_head_back -> this.finish()
            R.id.activity_chat_head_menu -> menuMenu.show()
            // activity_chat_foot
            R.id.activity_chat_foot_video_call -> {}
            R.id.activity_chat_foot_emoji_emotions -> onEmojiClicked()
            R.id.activity_chat_foot_function_keys -> onFunctionKeyClicked()
            // others
            else -> Toast.makeText(this, R.string.NotFinished, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * onclick emoji
     */
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

    /**
     * onclick function key
     */
    private fun onFunctionKeyClicked() {
        when (functionKeyType) {
            0 -> functionMenu.show()
            1 -> sendMessage()
        }
    }

    /**
     * onclick send message
     */
    private fun sendMessage() {
        val editText = findViewById<EditText>(R.id.activity_chat_foot_input)
        val text = editText.text.toString()
        if (text.isNotEmpty() && text != "") {
            val message = sendNormalText(text, System.currentTimeMillis())
            messageList.add(message)
            recyclerView.adapter?.notifyItemChanged(messageList.size - 1)
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
                        SRuntimeData.SMessageBox[tarUid] = "${this.getString(R.string.you)}: $text"
                    } else {
                        this.runOnUiThread {
                            Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()
            } catch (e: Exception) {
                Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.scrollToPosition(messageList.size - 1)
    }

    /**
     * on PopupMenu item clicked
     */
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_function_location -> {}
            else -> {}
        }
        return false
    }

}