package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuestionRepository(private val questionDao: QuestionDao) {
    val completedQuestionIds: Flow<Set<String>> = questionDao.getAllCompletedQuestions()
        .map { list -> list.map { it.questionId }.toSet() }

    suspend fun markCompleted(questionId: String) {
        questionDao.insertCompletedQuestion(CompletedQuestion(questionId))
    }
}
