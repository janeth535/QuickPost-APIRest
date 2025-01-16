package com.example.quickpost.data.api

import com.example.quickpost.data.model.Post
import retrofit2.http.*

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): Post

    @POST("posts")
    suspend fun createPost(@Body post: Post): Post

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") id: Post): Post

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int)
}