package com.example.quickpost.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quickpost.data.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen(
    postId: Int,
    viewModel: PostViewModel,
    onEditPost: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val selectedPost by viewModel.selectedPost.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Buscar o post específico
    LaunchedEffect(postId) {
        viewModel.fetchPostById(postId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Post") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            selectedPost?.let { post ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = post.title, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = post.body, style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { onEditPost(post.id!!) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Editar")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.deletePost(post.id!!)
                                onNavigateBack()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Excluir")
                        }
                    }
                }
            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Post não encontrado", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
