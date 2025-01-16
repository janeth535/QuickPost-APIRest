package com.example.quickpost.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickpost.data.model.Post
import com.example.quickpost.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _filteredPosts = MutableStateFlow<List<Post>>(emptyList())
    val filteredPosts: StateFlow<List<Post>> = _filteredPosts

    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost: StateFlow<Post?> = _selectedPost

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedPosts = repository.fetchPosts()
                _posts.value = fetchedPosts
                _filteredPosts.value = fetchedPosts
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchPostById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedPost.value = repository.fetchPostById(id)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createPost(post: Post) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.createPost(post)
                fetchPosts()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updatePost(post)
                fetchPosts()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletePost(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deletePost(id)
                fetchPosts()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchPosts(query: String) {
        _filteredPosts.update { posts ->
            if (query.isBlank()) {
                _posts.value
            } else {
                _posts.value.filter { it.title.contains(query, ignoreCase = true) }
            }
        }
    }
}
