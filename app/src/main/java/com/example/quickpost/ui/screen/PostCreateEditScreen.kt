package com.example.quickpost.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quickpost.data.model.Post
import com.example.quickpost.data.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCreateEditScreen(
    viewModel: PostViewModel,
    postToEdit: Post? = null, // Post existente para edição ou `null` para criação
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf(postToEdit?.title.orEmpty()) }
    var body by remember { mutableStateOf(postToEdit?.body.orEmpty()) }
    val isEditing = postToEdit != null
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Post" else "Criar Post") },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text("Conteúdo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (isEditing) {
                            viewModel.updatePost(
                                postToEdit!!.copy(title = title, body = body)
                            )
                        } else {
                            viewModel.createPost(
                                Post(title = title, body = body, userId = 1)
                            )
                        }
                        onNavigateBack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (isEditing) "Salvar Alterações" else "Criar Post")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}
