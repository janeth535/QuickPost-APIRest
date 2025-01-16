package com.example.quickpost.data.repository

import com.example.quickpost.data.model.Post
import com.example.quickpost.data.api.ApiClient

class PostRepository {
    private val api = ApiClient.apiService

    suspend fun fetchPosts() = api.getPosts()

    suspend fun fetchPostById(id: Int) = api.getPostById(id)

    suspend fun createPost(post: Post) = api.createPost(post)

    suspend fun updatePost(post: Post) = api.updatePost(post)

    suspend fun deletePost(id: Int) = api.deletePost(id)
}