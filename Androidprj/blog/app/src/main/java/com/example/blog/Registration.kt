package com.example.blog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.blog.R
import com.google.gson.JsonSerializationContext
import kotlinx.android.synthetic.main.activity_registration.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val blog = BlogApi()
        submitId.setOnClickListener {
            val username = usernameId.text.toString()
            val email = emailId.text.toString()
            val password = passwordId.text.toString()
            val password2 = passwordId2.text.toString()
            blog.createUserAuth("http://192.168.1.47:8000/auth/register/", email=email, password=password, password2 = password2,username = username, rdata=object :ResponseCreate {
                override fun data(data: JSONObject?, successful:Boolean) {
                    Handler(Looper.getMainLooper()).post(Runnable {
                        println(data)
                        if(successful) {
                            val intent = Intent(this@Registration, Login::class.java)
                            startActivity(intent)
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        } else {
                            for (i in data!!.keys()) {
                                val value = data.get(i)
                                if (value is JSONArray) {
                                    for (index in 0 until value.length()) {
                                        val text = value.get(index).toString()
                                        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    })
                }
            })
        }
    }
}