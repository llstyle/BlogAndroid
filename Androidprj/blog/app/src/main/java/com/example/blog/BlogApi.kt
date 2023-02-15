package com.example.blog

import com.google.gson.JsonNull
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException



class BlogApi() {
    private val client = OkHttpClient()

    fun getBlog(url:String, rdata: ResponseData) {
        val request = Request.Builder()
            .url(url)
            .build()

         var objc = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                var obj:JSONObject
                response.use {
                    val data: String = response.body!!.string()
                    val jsonData = JSONObject(data)
                    val jarray: JSONArray? = jsonData.getJSONArray("results")
                    val next: Any? = jsonData.get("next")
                    val previous: String? = jsonData.getString("previous")
                    rdata.data(jarray, next.toString(), previous)
                }
            }
        })
    }
    fun getBlogDetail(url:String, rdata: ResponseDataDetail, header: String = "") {
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "$header")
            .build()
        var objc = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                var obj:JSONObject
                response.use {
                    val jsonData: String = response.body!!.string()
                    val jobj = JSONObject(jsonData)
                    rdata.data(jobj)
                }
            }
        })
    }
    fun postAuth(url: String, rdata: ResponseDataDetail, email: String, password: String) {
        val formBody = FormBody.Builder()
            .add("email", email)
            .add("password", password)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                var obj:JSONObject
                response.use {
                    val jsonData: String = response.body!!.string()
                    val jobj = JSONObject(jsonData)
                    rdata.data(jobj)
                }
            }
        })
    }
    fun createUserAuth(url: String, username: String, email: String, password: String, password2: String, rdata: ResponseCreate) {
        val formBody = FormBody.Builder()
            .add("username", username)
            .add("email", email)
            .add("password", password)
            .add("password2", password2)

            .build()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                var obj:JSONObject
                response.use {
                    val jsonData: String = response.body!!.string()
                    val jobj = JSONObject(jsonData)
                    rdata.data(jobj, response.isSuccessful)
                }
            }
        })
    }
    fun createBlog(url: String, title: String,content: String, header: String = "", rdata: ResponseCreate) {
        val formBody = FormBody.Builder()
            .add("title", title)
            .add("content", content)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .addHeader("Authorization", "$header")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                var obj:JSONObject
                response.use {
                    val jsonData: String = response.body!!.string()
                    val jobj = JSONObject(jsonData)
                    rdata.data(jobj, response.isSuccessful)
                }
            }
        })
    }
}