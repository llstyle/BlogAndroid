package com.example.blog

import org.json.JSONObject

interface ResponseDataDetail {
    fun data(data: JSONObject?) {}
}