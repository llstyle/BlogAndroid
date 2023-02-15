package com.example.blog

import org.json.JSONArray

interface ResponseData {
    fun data(data: JSONArray?, next: String?, previous: String?) {}
}