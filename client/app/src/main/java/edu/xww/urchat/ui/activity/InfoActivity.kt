package edu.xww.urchat.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import edu.xww.urchat.R
import edu.xww.urchat.data.loader.ResourcesLoader
import edu.xww.urchat.data.runtime.LoginStatus
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.network.builder.ProtocBuilder
import edu.xww.urchat.network.source.DataSource
import edu.xww.urchat.ui.fragment.MineFragment
import edu.xww.urchat.util.Encode
import java.lang.Exception

class InfoActivity : AppCompatActivity() {

    private val select = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            try {
                Thread {
                    Log.d("InfoActivity/select", "get picture ${it.path}")
                    val filename = "${it.hashCode()}.png"
                    val photoBmp = MediaStore.Images.Media.getBitmap(contentResolver, it)
                    ResourcesLoader.saveImage(this, filename, photoBmp)

                    val res = DataSource.updateIcon(filename, photoBmp)

                    if (res is Result.Success) {
                        Log.d("InfoActivity/select", "success update icon")
                        LoginStatus.loggedInUser!!.icon = filename

                        MineFragment.updateUserIcon(filename)

                        this.runOnUiThread {
                            imageView.setImageBitmap(photoBmp)
                            Toast.makeText(this, R.string.update_positive, Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        this.runOnUiThread {
                            Toast.makeText(
                                this,
                                R.string.send_failed,
                                Toast.LENGTH_SHORT
                            )
                        }
                    }
                }.start()
            } catch (e: Exception) {
                this.runOnUiThread {
                    Toast.makeText(
                        this,
                        R.string.send_failed,
                        Toast.LENGTH_SHORT
                    )
                }
                e.printStackTrace()
            }
        } else {
            this.runOnUiThread { Toast.makeText(this, R.string.cancel, Toast.LENGTH_SHORT) }
        }
    }

    private lateinit var displayNameE: EditText
    private lateinit var pwdE: EditText
    private lateinit var imageView: ImageView
    private lateinit var submit: Button
    private lateinit var cancel: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        imageView = findViewById(R.id.activity_info_img)
        ResourcesLoader.setImageBitmap(this, imageView, LoginStatus.loggedInUser?.icon ?: "")
        imageView.setOnClickListener { select.launch("image/*") }

        displayNameE = findViewById(R.id.activity_info_display_name)
        pwdE = findViewById(R.id.activity_info_new_password)
        submit = findViewById(R.id.activity_info_commit)

        submit.setOnClickListener {
            Log.d("InfoActivity/submit", "user try submit")
            var pwd = pwdE.text.toString()
            val d = displayNameE.text.toString()
            Thread {
                val p = ProtocBuilder()
                if (d.isNotEmpty() && d != "" && d != LoginStatus.loggedInUser!!.displayName) {
                    p.putPairs("displayName", "displayName")
                }
                if (pwd.length >= 6) {
                    p.putPairs("upassword", pwd)
                    pwd = Encode.hash(pwd)
                }
                val b = p.requireUpdate().buildValid()

                val res = DataSource.send(b)
                if (res is Result.Success) {
                    Log.d("InfoActivity/submit", "submit success")
                    displayNameE.setText("")
                    pwdE.setText("")

                    this.runOnUiThread {
                        if (pwd.length >= 6 && d.isNotEmpty() && d != "") {
                            LoginStatus.loggedInUser!!.displayName = d
                            MineFragment.updateUserInfo(d)
                            Toast.makeText(this, R.string.update_password, Toast.LENGTH_SHORT).show()
                        }else if (pwd.length >= 6) {
                            Toast.makeText(this, R.string.update_password, Toast.LENGTH_SHORT).show()
                        } else if (d.isNotEmpty() && d != ""){
                            LoginStatus.loggedInUser!!.displayName = d
                            MineFragment.updateUserInfo(d)
                            Toast.makeText(this, R.string.update_positive, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, R.string.update_positive, Toast.LENGTH_SHORT).show()
                        }
                    }

                    if (pwd.length >= 6) {
                        val data = getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                        data.clear()
                        data.apply()
                    }
                }
            }.start()
        }

        cancel = findViewById(R.id.activity_info_exit)
        cancel.setOnClickListener { this.finish() }

    }
}