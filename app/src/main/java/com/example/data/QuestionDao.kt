package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM completed_questions")
    fun getAllCompletedQuestions(): Flow<List<CompletedQuestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedQuestion(question: CompletedQuestion)
}
