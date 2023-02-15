package com.example.blog.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONObject


class Home : Fragment() {
    var post = mutableListOf<Posts>()
    var urlbl: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val a = BlogApi()
        Handler(Looper.getMainLooper()).post(Runnable {
            val recyclerView: RecyclerView = mainContentView
            recyclerView.layoutManager = LinearLayoutManager(context)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val a = BlogApi()
        val recyclerView: RecyclerView = mainContentView
        recyclerView.layoutManager = LinearLayoutManager(context)
        getBlog(a, listp = post, recyclerView = recyclerView)
        refreshButton.setOnClickListener {
            post.clear()
            recyclerView.adapter?.notifyDataSetChanged()
            getBlog(a, listp = post, recyclerView = recyclerView)
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getBlog(a,urlbl.toString(), listp = post, recyclerView = recyclerView)
                }
            }
        })
    }
    fun getBlog(a: BlogApi, url: String = "http://192.168.1.47:8000/api/v1/blog/", listp:MutableList<Posts>, recyclerView: RecyclerView) {
        a.getBlog(url, object : ResponseData {
            override fun data(data: JSONArray?, next: String?, previous: String?) {
                Handler(Looper.getMainLooper()).post(Runnable {
                    progress?.removeAllViews()
                })
                for (i in 0 until data!!.length()) {
                    var obj: JSONObject = data.getJSONObject(i)
                    val title = obj.getString("title")
                    val author = obj.getString("author")
                    val id = obj?.get("id").toString().toInt()
                    if(Posts(title, author, id) !in listp) {
                        listp.add(Posts(title, author, id))
                    }
                    if(next == "null") {
                        urlbl = "http://192.168.1.47:8000/api/v1/blog/"
                    } else {
                        urlbl = next.toString()
                    }
                    Handler(Looper.getMainLooper()).post(Runnable {
                        var adapter = CustomRecyclerAdapter(listp)
                        recyclerView.adapter = adapter
                        adapter.setOnItemClickListener(object: CustomRecyclerAdapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val intent = Intent(context, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.ID, listp.get(position).id)
                                startActivity(intent)
                                activity?.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

                            }
                        })
                    })

                }
            }
        })
    }

}