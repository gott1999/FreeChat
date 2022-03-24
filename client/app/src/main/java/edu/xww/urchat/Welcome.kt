package edu.xww.urchat

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity

class Welcome : AppCompatActivity() {

    private var startCode = 0

    fun getStartCode(): Int {
        return startCode
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        // 延迟执行
        // Handler(Looper.getMainLooper()).postDelayed({ launcher() }, 3000)

        // 直接跳转
        launcher()
    }

    private fun launcher() {
        if (startCode >= 0) {
            startActivity(Intent(this, edu.xww.urchat.ui.MainActivity::class.java))
            finish()
        }
    }

}