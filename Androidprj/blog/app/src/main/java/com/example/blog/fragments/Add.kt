package com.example.blog.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.blog.BlogApi
import com.example.blog.MainActivity
import com.example.blog.R
import com.example.blog.ResponseCreate
import kotlinx.android.synthetic.main.fragment_add.*
import org.json.JSONArray
import org.json.JSONObject


class Add : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val blog = BlogApi()
        buttonblog.setOnClickListener {
            val tokenA = loadData().toString()
            val title = title_text.text.toString()
            val content = edit_text.text.toString()
            blog.createBlog("http://192.168.1.47:8000/api/v1/blogCreate/", title=title, content=content, header="Token $tokenA" , rdata=object :
                ResponseCreate {
                override fun data(data: JSONObject?, successful:Boolean) {
                    Handler(Looper.getMainLooper()).post(Runnable {
                        println(data)
                        if(successful) {
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            for (i in data!!.keys()) {
                                val value = data.get(i)
                                if (value is JSONArray) {
                                    for (index in 0 until value.length()) {
                                        val text = value.get(index).toString()
                                        Toast.makeText(activity?.applicationContext, text, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    })
                }
            })
        }
    }
    private fun loadData(): String? {
        val tokenPref = activity?.getSharedPreferences("TokenPref", Context.MODE_PRIVATE)
        val tokenAuth = tokenPref?.getString("TOKEN_KEY", null)
        println("AAA $tokenAuth")
        return tokenAuth
    }

}