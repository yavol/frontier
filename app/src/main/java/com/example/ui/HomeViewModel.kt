package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.QuestionRepository
import com.example.domain.Question
import com.example.domain.QuestionProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val questions: List<QuestionItem> = emptyList()
)

data class QuestionItem(
    val question: Question,
    val isCompleted: Boolean
)

class HomeViewModel(private val repository: QuestionRepository) : ViewModel() {
    
    val uiState: StateFlow<HomeUiState> = repository.completedQuestionIds
        .combine(kotlinx.coroutines.flow.flowOf(QuestionProvider.questions)) { completedIds, questions ->
            HomeUiState(
                questions = questions.map { q ->
                    QuestionItem(
                        question = q,
                        isCompleted = completedIds.contains(q.id)
                    )
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(QuestionProvider.questions.map { QuestionItem(it, false) })
        )
}

class HomeViewModelFactory(private val repository: QuestionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
