package edu.xww.urchat.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import edu.xww.urchat.R
import edu.xww.urchat.data.struct.Result
import edu.xww.urchat.data.user.LoginDataSource
import edu.xww.urchat.data.user.LoginRepository
import edu.xww.urchat.tools.Encode.hash

class LoginActivity : AppCompatActivity() {

    private val loginRepository = LoginRepository(LoginDataSource())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username: EditText = findViewById(R.id.login_username)
        val password: EditText = findViewById(R.id.login_password)
        val loginButton: Button = findViewById(R.id.login_bottom)

        loginButton.setOnClickListener {
            val res = loginRepository.login(
                username.text.toString(),
                hash(password.text.toString())
            )
            if (res is Result.Success) {
                startActivity(Intent(this, Welcome::class.java))
                finish()
            } else {
                Toast.makeText(this, "Login Fail!", Toast.LENGTH_SHORT).show()
            }
        }
    }


}