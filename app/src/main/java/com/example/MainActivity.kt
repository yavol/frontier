package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.QuestionRepository
import com.example.ui.HomeScreen
import com.example.ui.HomeViewModel
import com.example.ui.HomeViewModelFactory
import com.example.ui.QuestionScreen
import com.example.ui.QuestionViewModel
import com.example.ui.QuestionViewModelFactory
import com.example.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "frontier-foundations-db"
        ).build()
        
        val repository = QuestionRepository(db.questionDao())

        setContent {
            AppTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        val homeViewModel: HomeViewModel = viewModel(
                            factory = HomeViewModelFactory(repository)
                        )
                        HomeScreen(
                            viewModel = homeViewModel,
                            onQuestionClick = { questionId ->
                                navController.navigate("question/$questionId")
                            }
                        )
                    }
                    composable("question/{questionId}") { backStackEntry ->
                        val questionId = backStackEntry.arguments?.getString("questionId") ?: return@composable
                        val questionViewModel: QuestionViewModel = viewModel(
                            key = questionId,
                            factory = QuestionViewModelFactory(repository, questionId)
                        )
                        QuestionScreen(
                            viewModel = questionViewModel,
                            onBack = { navController.popBackStack() },
                            onDone = {
                                navController.popBackStack("home", inclusive = false)
                            }
                        )
                    }
                }
            }
        }
    }
}
