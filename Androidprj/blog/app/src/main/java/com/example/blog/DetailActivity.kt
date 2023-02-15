package com.example.blog

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONArray
import org.json.JSONObject


class DetailActivity: AppCompatActivity() {
    companion object {
        var ID: String = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val getblog = BlogApi()
        val mainHandler = Handler(Looper.getMainLooper())
        getblog.getBlogDetail("http://192.168.1.47:8000/api/v1/blogDetail/${intent.getIntExtra(ID, 0)}/", object :ResponseDataDetail {
            override fun data(data: JSONObject?) {
                mainHandler.post(Runnable {
                    author.text = data?.get("author").toString()
                    titleid.text = data?.get("title").toString()
                    contentid.text = data?.get("content").toString()
                })
                var com = data?.get("comments")
                if (com is JSONArray) {
                    for (i in 0 until com!!.length()) {
                        var obj: JSONObject? = com?.getJSONObject(i)
                        var title = TextView(this@DetailActivity)
                        title.text = "${obj?.get("author").toString()} | ${obj?.get("comment_text").toString()}"
                        title.setTextSize(22f);
                        title.setBackgroundColor(Color.parseColor("#cfd8dc"))
                        title.setTypeface(title.getTypeface(), Typeface.BOLD);
                        mainHandler.post(Runnable {
                            contentlayout.addView(title)
                        })
                    }
                }

            }
        })
    }
}