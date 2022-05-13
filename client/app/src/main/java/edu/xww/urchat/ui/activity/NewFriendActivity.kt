package edu.xww.urchat.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.xww.urchat.R
import edu.xww.urchat.data.runtime.SLoginStatus
import edu.xww.urchat.data.runtime.SRuntimeData
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.network.builder.MessageBuilder
import edu.xww.urchat.network.source.DataSource
import edu.xww.urchat.ui.adapter.recyclerview.NewFriendAdaptor

class NewFriendActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var button: Button
    private lateinit var et: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_friend)

        findViewById<TextView>(R.id.common_head_title).text = this.getText(R.string.newfriend)
        setParams()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun setParams() {
        recyclerView = findViewById(R.id.activity_new_friend_recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = NewFriendAdaptor(this, SRuntimeData.SNewContacts)

        et = findViewById(R.id.activity_new_friend_input)
        button = findViewById(R.id.activity_new_friend_button)
        button.setOnClickListener {
            val uid = et.text.toString()
            if (uid.length >= 6) {
                try {
                    Thread {
                        val m = MessageBuilder()
                                // 此时uid承载的是uname
                            .setDesUid(uid)
                            .setMessage("${SLoginStatus.userBasicData!!.displayName} 请求添加为好友")
                            .setType(MessageBuilder.Type.ADD)
                            .build()

                        val res = DataSource.sendMessage(m)

                        this.runOnUiThread {
                            if (res is Result.Success) {
                                // success
                                Toast.makeText(this, R.string.send_success, Toast.LENGTH_SHORT)
                                    .show()
                                et.setText("")
                            } else {
                                Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                    }.start()
                } catch (e: Exception) {
                    Log.d("Send Message", e.toString())
                    Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, R.string.uname_too_short, Toast.LENGTH_SHORT).show()
            }
        }
    }
}