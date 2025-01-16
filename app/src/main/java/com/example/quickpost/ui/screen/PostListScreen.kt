package com.example.quickpost.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quickpost.data.viewmodel.PostViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    viewModel: PostViewModel,
    onPostClick: (Int) -> Unit,
    onCreateNewPost: () -> Unit
) {
    val posts by viewModel.filteredPosts.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Buscar os dados sempre que a tela for exibida
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                viewModel.fetchPosts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Posts") },
                actions = {
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.searchPosts(it)
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                            .padding(horizontal = 8.dp),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text("Pesquisar...", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            innerTextField()
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateNewPost) {
                Text("+")
            }
        }
    ) { padding ->
        if (isLoading) {
            // Exibe um indicador de carregamento
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(posts) { post ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPostClick(post.id!!) },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = post.body, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
