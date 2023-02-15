package com.example.blog

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val blog = BlogApi()
        submitId.setOnClickListener {
            val email = emailId.text.toString()
            val password = passwordId.text.toString()
            blog.postAuth("http://192.168.1.47:8000/auth/token/login/", email=email, password=password, rdata=object :ResponseDataDetail {
                override fun data(data: JSONObject?) {
                    Handler(Looper.getMainLooper()).post(Runnable {
                        if (data!!.has("auth_token")) {
                            saveData(data?.get("auth_token").toString())
                            val intent = Intent(this@Login, MainActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        } else {
                            val text = "Password or email failed"
                            val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_LONG)
                            toast.show()
                        }
                    })
                }
            })
        }

        createacc.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
           overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }
    private fun saveData(token: String) {
        val sharedPrefs  = getSharedPreferences("TokenPref", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.apply {
            putString("TOKEN_KEY", token)
        }.apply()
    }
}