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

class LoginActivity : AppCompatActivity(), View.OnClickListener {

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
        val username: EditText = findViewById(R.id.login_username)
        val password: EditText = findViewById(R.id.login_password)

        if (username.text.length > 6 && password.text.length > 6) {
            val res = LoginRepository.login(
                username.text.toString(),
                hash(password.text.toString())
            )
            if (res is Result.Success) {
                startActivity(Intent(this, Welcome::class.java))
                finish()
            } else {
                Toast.makeText(this, res.toString(), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.uname_pwd_too_short, Toast.LENGTH_SHORT).show()
        }
    }

    private fun register() {
        val username: EditText = findViewById(R.id.login_username)
        val password: EditText = findViewById(R.id.login_password)

        if (username.text.length > 6 && password.text.length > 6) {
            val res = LoginRepository.register(
                username.text.toString(),
                hash(password.text.toString())
            )
            if (res is Result.Success) {
                startActivity(Intent(this, Welcome::class.java))
                finish()
            } else {
                Toast.makeText(this, res.toString(), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.uname_pwd_too_short, Toast.LENGTH_SHORT).show()
        }
    }

}