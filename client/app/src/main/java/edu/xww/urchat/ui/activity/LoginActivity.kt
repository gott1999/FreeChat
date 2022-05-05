package edu.xww.urchat.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import edu.xww.urchat.R
import edu.xww.urchat.data.runtime.LoginRepository
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.tools.Encode.hash
import edu.xww.urchat.tools.IpChecker
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(1)

    private val lock = ReentrantLock()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.login_button).setOnClickListener(this)
        findViewById<Button>(R.id.register_button).setOnClickListener(this)
    }

    override fun onClick(p0: View?) = when (p0?.id) {
        R.id.login_button -> login()
        R.id.register_button -> register()
        else -> {}
    }

    private fun login() {
        val unameEditText: EditText = findViewById(R.id.login_username)
        val uname = unameEditText.text.toString()

        val passwordEditText: EditText = findViewById(R.id.login_password)
        val upassword = passwordEditText.text.toString()

        val serverEditText: EditText = findViewById(R.id.login_server_ip)
        val server = serverEditText.text.toString()

        if (!IpChecker.checkIpv4(server)) {
            Toast.makeText(this, R.string.ip_error, Toast.LENGTH_SHORT).show()
            return
        }

        if (uname.length < 6 && upassword.length < 6) {
            Toast.makeText(this, R.string.uname_pwd_too_short, Toast.LENGTH_SHORT).show()
            return
        }

        executorService.execute{
            if (lock.isLocked) return@execute
            lock.lock()

            val res = LoginRepository.login(server, 25565, uname, hash(upassword))
            if (res is Result.Success) {
                startActivity(Intent(this, Welcome::class.java))
                finish()
            } else {
                this.runOnUiThread{
                    Toast.makeText(this, res.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            lock.unlock()
        }
    }

    private fun register() {
        val usernameEditText: EditText = findViewById(R.id.login_username)
        val uname = usernameEditText.text.toString()

        val passwordEditText: EditText = findViewById(R.id.login_password)
        val upassword = passwordEditText.text.toString()

        val serverEditText: EditText = findViewById(R.id.login_server_ip)
        val server = serverEditText.text.toString()

        if (!IpChecker.checkIpv4(server)) {
            Toast.makeText(this, R.string.ip_error, Toast.LENGTH_SHORT).show()
            return
        }

        if (uname.length < 6 && upassword.length < 6) {
            Toast.makeText(this, R.string.uname_pwd_too_short, Toast.LENGTH_SHORT).show()
            return
        }

        executorService.execute {
            if (lock.isLocked) return@execute
            lock.lock()

            val res = LoginRepository.register(server, 25565, uname, hash(upassword))
            if (res is Result.Success) {
                startActivity(Intent(this, Welcome::class.java))
                finish()
            } else {
                this.runOnUiThread{
                    Toast.makeText(this, res.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            lock.unlock()
        }
    }


}