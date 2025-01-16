package com.example.quickpost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quickpost.data.viewmodel.PostViewModel
import com.example.quickpost.ui.screen.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            setContent {
                val navController = rememberNavController()
                val viewModel = PostViewModel()

                NavHost(navController = navController, startDestination = "posts") {
                    // Tela de lista de posts
                    composable("posts") {
                        PostListScreen(
                            viewModel = viewModel,
                            onPostClick = { postId -> navController.navigate("postDetails/$postId") },
                            onCreateNewPost = { navController.navigate("createPost") }
                        )
                    }

                    // Tela de detalhes de um post
                    composable(
                        route = "postDetails/{postId}",
                        arguments = listOf(navArgument("postId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val postId = backStackEntry.arguments?.getInt("postId") ?: -1
                        if (postId == -1) {
                            // Log para debugging
                            println("PostDetailsScreen: postId inválido")
                            navController.popBackStack()
                            return@composable
                        }

                        PostDetailsScreen(
                            postId = postId,
                            viewModel = viewModel,
                            onEditPost = { postId -> navController.navigate("editPost/$postId") },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // Tela de criação de um novo post
                    composable("createPost") {
                        PostCreateEditScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // Tela de edição de um post existente
                    composable(
                        route = "editPost/{postId}",
                        arguments = listOf(navArgument("postId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val postId = backStackEntry.arguments?.getInt("postId") ?: -1
                        if (postId == -1) {
                            // Log para debugging
                            println("EditPostScreen: postId inválido")
                            navController.popBackStack()
                            return@composable
                        }

                        val posts by viewModel.posts.collectAsState() // Evita erro ao acessar StateFlow vazio

                        val postToEdit = posts.find { it.id == postId }
                        if (postToEdit != null) {
                            PostCreateEditScreen(
                                viewModel = viewModel,
                                postToEdit = postToEdit,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        } else {
                            // Log para debugging
                            println("EditPostScreen: Post não encontrado para postId $postId")
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}
