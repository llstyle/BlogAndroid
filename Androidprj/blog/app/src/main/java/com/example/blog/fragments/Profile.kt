package com.example.blog.fragments

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.blog.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONArray
import org.json.JSONObject


class Profile : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(loadData() == null) {
            val intent = Intent(context, Login::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        val tokenA = loadData().toString()
        val getblog = BlogApi()
        val mainHandler = Handler(Looper.getMainLooper())
        if(loadData() != null) {
            getblog.getBlogDetail(
                "http://192.168.1.47:8000/api/v1/profileDetail/",
                header = "Token $tokenA",
                rdata = object : ResponseDataDetail {
                    override fun data(data: JSONObject?) {
                        mainHandler.post(Runnable {
                            authorid.text = data?.get("username").toString()
                        })
                        var com = data!!.get("blogs")
                        if (com is JSONArray) {
                            for (i in 0 until com!!.length()) {
                                var obj: JSONObject? = com?.getJSONObject(i)
                                var title = TextView(context)
                                title.text = "${obj?.get("title").toString()}"
                                val p = ActionBar.LayoutParams(
                                    ActionBar.LayoutParams.MATCH_PARENT,
                                    ActionBar.LayoutParams.WRAP_CONTENT
                                )
                                p.setMargins(0, 10, 0, 10)
                                title.setTextSize(22f);
                                title.setPadding(10, 10, 10, 10)
                                title.setTextColor(Color.parseColor("#ffffff"))
                                title.setBackgroundColor(Color.parseColor("#651fff"))
                                title.layoutParams = p
                                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                                mainHandler.post(Runnable {
                                    blogsAuthor.addView(title)
                                    title.setOnClickListener {
                                        val intent = Intent(context, DetailActivity::class.java)
                                        intent.putExtra(
                                            DetailActivity.ID,
                                            obj?.get("id").toString().toInt()
                                        )
                                        startActivity(intent)
                                        activity?.overridePendingTransition(
                                            android.R.anim.slide_in_left,
                                            android.R.anim.slide_out_right
                                        )
                                    }
                                })
                            }
                        }

                    }
                })
        }
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    private fun loadData(): String? {
        val tokenPref = activity?.getSharedPreferences("TokenPref", Context.MODE_PRIVATE)
        val tokenAuth = tokenPref?.getString("TOKEN_KEY", null)
        println("AAA $tokenAuth")
        return tokenAuth
    }
}