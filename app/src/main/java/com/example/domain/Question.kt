package com.example.domain

data class Option(
    val id: String,
    val text: String
)

data class Question(
    val id: String,
    val homeCardQuestion: String,
    val fullQuestion: String,
    val imageRes: Int,
    
    // Step 1: Compare
    val compareOptions: List<Option>,
    val correctCompareOptionId: String,
    val compareExplanation: String,
    
    // Step 2: Find the problem
    val diagnosisOptions: List<Option>,
    val correctDiagnosisOptionId: String,
    val diagnosisExplanation: String,
    
    // Step 3: Fix the answer
    val replacementOptions: List<Option>,
    val correctReplacementOptionId: String,
    val completeCorrectedAnswer: String,
    
    val takeaway: String,
    val sourceUrls: List<String>
)
