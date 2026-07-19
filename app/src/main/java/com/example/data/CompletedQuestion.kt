package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_questions")
data class CompletedQuestion(
    @PrimaryKey val questionId: String
)
