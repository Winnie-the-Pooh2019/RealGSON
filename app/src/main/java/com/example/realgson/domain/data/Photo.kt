package com.example.realgson.domain.data

data class Photo(
    val id: String, val owner: String, val secret: String,
    val server: String, val farm: Int, val title: String,
    val isPublic: Int, val isFriend: Int, val isFamily: Int)
//    val farm: Int, val server: String,
//    val id: String, val secret: String)