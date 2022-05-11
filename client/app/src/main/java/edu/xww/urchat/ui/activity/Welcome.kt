package edu.xww.urchat.ui.activity

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import edu.xww.urchat.R
import edu.xww.urchat.data.loader.LoaderManager
import edu.xww.urchat.data.runtime.LoginStatus
import edu.xww.urchat.data.runtime.RunTimeData


class Welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        if (LoginStatus.isLoggedIn()) {
            update()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun update() {
        LoaderManager.updateContact()
        LoaderManager.updateMessage()
    }

}