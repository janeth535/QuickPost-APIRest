package com.example.quickpost.data.model

data class Post(
    val id: Int? = null,
    val title: String,
    val body: String,
    val userId: Int
)