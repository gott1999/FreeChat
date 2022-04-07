package edu.xww.urchat.ui.activity

import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.xww.urchat.R
import edu.xww.urchat.data.runtime.MessageData
import edu.xww.urchat.data.runtime.UserData.isLoggedIn
import java.lang.Exception

class Welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        if (!isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            update()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun update() {
        MessageData.update()
    }

}