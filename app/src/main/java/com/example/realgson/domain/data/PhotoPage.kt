package com.example.realgson.domain.data

import com.google.gson.JsonArray

class PhotoPage(val page: Int, val pages: Int, val perPage: Int, val total: Int, val photo: JsonArray)