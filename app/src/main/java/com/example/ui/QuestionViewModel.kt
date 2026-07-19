package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.QuestionRepository
import com.example.domain.Option
import com.example.domain.Question
import com.example.domain.QuestionProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class QuestionStep {
    COMPARE,
    DIAGNOSIS,
    REPLACEMENT,
    DONE
}

data class QuestionUiState(
    val question: Question,
    val step: QuestionStep = QuestionStep.COMPARE,
    // Step 1
    val compareOptions: List<Option>,
    val selectedCompareOptionId: String? = null,
    val showCompareExplanation: Boolean = false,
    // Step 2
    val selectedDiagnosisOptionId: String? = null,
    val showDiagnosisExplanation: Boolean = false,
    // Step 3
    val selectedReplacementOptionId: String? = null,
    val showReplacementExplanation: Boolean = false
)

class QuestionViewModel(
    private val repository: QuestionRepository,
    private val questionId: String
) : ViewModel() {

    private val question = QuestionProvider.questions.first { it.id == questionId }
    
    // We randomize once when the question begins
    private val randomizedCompareOptions = question.compareOptions.shuffled()

    private val _uiState = MutableStateFlow(
        QuestionUiState(
            question = question,
            compareOptions = randomizedCompareOptions
        )
    )
    val uiState: StateFlow<QuestionUiState> = _uiState.asStateFlow()

    fun selectCompareOption(optionId: String) {
        if (_uiState.value.selectedCompareOptionId != null) return // Already selected
        _uiState.update { it.copy(selectedCompareOptionId = optionId, showCompareExplanation = true) }
    }

    fun proceedToDiagnosis() {
        _uiState.update { it.copy(step = QuestionStep.DIAGNOSIS) }
    }

    fun selectDiagnosisOption(optionId: String) {
        if (_uiState.value.selectedDiagnosisOptionId != null) return
        _uiState.update { it.copy(selectedDiagnosisOptionId = optionId, showDiagnosisExplanation = true) }
    }

    fun proceedToReplacement() {
        _uiState.update { it.copy(step = QuestionStep.REPLACEMENT) }
    }

    fun selectReplacementOption(optionId: String) {
        if (_uiState.value.selectedReplacementOptionId != null) return
        _uiState.update { it.copy(selectedReplacementOptionId = optionId, showReplacementExplanation = true) }
        
        // Mark as completed when the final answer is selected
        viewModelScope.launch {
            repository.markCompleted(questionId)
        }
    }

    fun getWrongAnswerLabel(): String {
        val wrongOptionIndex = _uiState.value.compareOptions.indexOfFirst { it.id != question.correctCompareOptionId }
        return if (wrongOptionIndex == 0) "A" else "B"
    }
    
    fun getWrongAnswerText(): String {
        return _uiState.value.compareOptions.first { it.id != question.correctCompareOptionId }.text
    }
}

class QuestionViewModelFactory(
    private val repository: QuestionRepository,
    private val questionId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuestionViewModel(repository, questionId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
