package edu.xww.urchat.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import edu.xww.urchat.R
import edu.xww.urchat.data.runtime.LoginStatus
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.util.Encode.hash
import edu.xww.urchat.util.IpChecker
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(1)

    private val lock = ReentrantLock()

    var count = 0

    private lateinit var unameEditText:EditText

    private lateinit var passwordEditText:EditText

    private lateinit var serverEditText:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.login_button).setOnClickListener(this)
        findViewById<Button>(R.id.register_button).setOnClickListener(this)

        unameEditText = findViewById(R.id.login_username)
        passwordEditText = findViewById(R.id.login_password)
        serverEditText = findViewById(R.id.login_server_ip)

        checkRef()
    }

    private fun checkRef() {
        val data = getSharedPreferences("user", Context.MODE_PRIVATE)
        val server = data.getString("server", null)
        val uname = data.getString("uname", null)
        val upassword = data.getString("upassword", null)
        if (server != null && uname != null && upassword != null) {
            lock.lock()
            executorService.execute {
                val res = LoginStatus.login(server, 25565, uname, upassword)
                if (res is Result.Success) {
                    startActivity(Intent(this, Welcome::class.java))
                    finish()
                } else {
                    this.runOnUiThread {
                        Toast.makeText(this, res.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            }
            lock.unlock()
        }
    }

    override fun onClick(p0: View?) = when (p0?.id) {
        R.id.login_button -> login()
        R.id.register_button -> register()
        else -> {}
    }

    @SuppressLint("CommitPrefEdits")
    private fun login() {
        count++
        val server = serverEditText.text.toString()
        val uname = unameEditText.text.toString()
        val upassword = passwordEditText.text.toString()


        if (!IpChecker.checkIpv4(server)) {
            Toast.makeText(this, R.string.ip_error, Toast.LENGTH_SHORT).show()
            return
        }

        if (uname.length < 6 && upassword.length < 6) {
            Toast.makeText(this, R.string.uname_pwd_too_short, Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("LoginActivity/Login", "User $uname login")

        if (lock.isLocked) {
            Log.i("LoginActivity/Login", "User click more than one time")
            return
        }

        lock.lock()

        executorService.execute {
            val p = hash(upassword)
            val res = LoginStatus.login(server, 25565, uname, p)
            if (res is Result.Success) {
                startActivity(Intent(this, Welcome::class.java))
                finish()
            } else {
                this.runOnUiThread {
                    Toast.makeText(this, res.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            val data = getSharedPreferences("user", Context.MODE_PRIVATE).edit()
            data.putString("server", server)
            data.putString("uname", uname)
            data.putString("upassword", p)
        }
        lock.unlock()
    }

    @SuppressLint("CommitPrefEdits")
    private fun register() {
        count++
        val server = serverEditText.text.toString()
        val uname = unameEditText.text.toString()
        val upassword = passwordEditText.text.toString()

        if (!IpChecker.checkIpv4(server)) {
            Toast.makeText(this, R.string.ip_error, Toast.LENGTH_SHORT).show()
            return
        }

        if (uname.length < 6 && upassword.length < 6) {
            Toast.makeText(this, R.string.uname_pwd_too_short, Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("LoginActivity/Register", "User $uname register ")

        if (lock.isLocked) {
            Log.i("LoginActivity/Register", "User click more than one time")
            return
        }

        lock.lock()

        executorService.execute {
            val p = hash(upassword)
            val res = LoginStatus.register(server, 25565, uname, p)
            if (res is Result.Success) {
                startActivity(Intent(this, Welcome::class.java))
                finish()
            } else {
                this.runOnUiThread {
                    Toast.makeText(this, res.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            val data = getSharedPreferences("user", Context.MODE_PRIVATE).edit()
            data.putString("server", server)
            data.putString("uname", uname)
            data.putString("upassword", p)
        }
        lock.unlock()
    }

}