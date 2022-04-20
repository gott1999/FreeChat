package edu.xww.urchat.ui.activity

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import edu.xww.urchat.R
import edu.xww.urchat.data.runtime.LoginRepository
import edu.xww.urchat.data.runtime.RunTimeData


class Welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        if (LoginRepository.isLoggedIn()) {
            update()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun update() {
        RunTimeData.update()
    }

}