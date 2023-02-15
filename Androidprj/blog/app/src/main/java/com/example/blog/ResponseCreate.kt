package com.example.blog

import org.json.JSONObject

interface ResponseCreate {
    fun data(data: JSONObject?, successful:Boolean) {}
}