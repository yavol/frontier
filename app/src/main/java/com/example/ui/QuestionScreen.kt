package com.example.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.Option
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    viewModel: QuestionViewModel,
    onBack: () -> Unit,
    onDone: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    val stepText = when (uiState.step) {
                        QuestionStep.COMPARE -> "1 of 3"
                        QuestionStep.DIAGNOSIS -> "2 of 3"
                        QuestionStep.REPLACEMENT, QuestionStep.DONE -> "3 of 3"
                    }
                    Text(stepText, style = MaterialTheme.typography.labelLarge)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        androidx.compose.animation.AnimatedContent(
            targetState = uiState.step,
            label = "step_transition",
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { step ->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                when (step) {
                    QuestionStep.COMPARE -> CompareStep(uiState, viewModel)
                    QuestionStep.DIAGNOSIS -> DiagnosisStep(uiState, viewModel)
                    QuestionStep.REPLACEMENT, QuestionStep.DONE -> ReplacementStep(uiState, viewModel, onDone)
                }
            }
        }
    }
}

@Composable
fun CompareStep(uiState: QuestionUiState, viewModel: QuestionViewModel) {
    Image(
        painter = painterResource(id = uiState.question.imageRes),
        contentDescription = "Illustration for ${uiState.question.homeCardQuestion}",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(MaterialTheme.shapes.medium),
        contentScale = ContentScale.Crop
    )
    
    Spacer(modifier = Modifier.height(8.dp))
    Text("Compare", style = MaterialTheme.typography.headlineMedium)
    Text(uiState.question.fullQuestion, style = MaterialTheme.typography.bodyLarge)
    
    Spacer(modifier = Modifier.height(8.dp))
    Text("Which answer is correct?", style = MaterialTheme.typography.titleMedium)
    Text("Choose one answer before the explanation is revealed.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    
    uiState.compareOptions.forEachIndexed { index, option ->
        val label = if (index == 0) "A" else "B"
        val isSelected = uiState.selectedCompareOptionId == option.id
        val isCorrect = option.id == uiState.question.correctCompareOptionId
        
        AnswerCard(
            label = label,
            text = option.text,
            isSelected = isSelected,
            isCorrect = isCorrect,
            showResult = uiState.showCompareExplanation,
            onClick = { viewModel.selectCompareOption(option.id) }
        )
    }

    if (uiState.showCompareExplanation) {
        ExplanationCard(text = uiState.question.compareExplanation)
        Button(
            onClick = { viewModel.proceedToDiagnosis() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}

@Composable
fun DiagnosisStep(uiState: QuestionUiState, viewModel: QuestionViewModel) {
    Text("Find the problem", style = MaterialTheme.typography.headlineMedium)
    
    // Visually quieter reference card
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Answer ${viewModel.getWrongAnswerLabel()}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(viewModel.getWrongAnswerText(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text("Which diagnosis best identifies the two main problems in Answer ${viewModel.getWrongAnswerLabel()}?", style = MaterialTheme.typography.titleMedium)
    Text("Choose the most important diagnosis.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    
    uiState.question.diagnosisOptions.forEach { option ->
        val isSelected = uiState.selectedDiagnosisOptionId == option.id
        val isCorrect = option.id == uiState.question.correctDiagnosisOptionId
        
        AnswerCard(
            label = null,
            text = option.text,
            isSelected = isSelected,
            isCorrect = isCorrect,
            showResult = uiState.showDiagnosisExplanation,
            onClick = { viewModel.selectDiagnosisOption(option.id) }
        )
    }

    if (uiState.showDiagnosisExplanation) {
        ExplanationCard(text = uiState.question.diagnosisExplanation)
        Button(
            onClick = { viewModel.proceedToReplacement() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}

@Composable
fun ReplacementStep(uiState: QuestionUiState, viewModel: QuestionViewModel, onDone: () -> Unit) {
    Text("Fix Answer ${viewModel.getWrongAnswerLabel()}", style = MaterialTheme.typography.headlineMedium)
    
    Text("Which change makes the answer technically complete?", style = MaterialTheme.typography.titleMedium)
    Text("Choose the single best replacement.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    
    uiState.question.replacementOptions.forEach { option ->
        val isSelected = uiState.selectedReplacementOptionId == option.id
        val isCorrect = option.id == uiState.question.correctReplacementOptionId
        
        AnswerCard(
            label = null,
            text = option.text,
            isSelected = isSelected,
            isCorrect = isCorrect,
            showResult = uiState.showReplacementExplanation,
            onClick = { viewModel.selectReplacementOption(option.id) }
        )
    }

    if (uiState.showReplacementExplanation) {
        Text("Complete corrected answer", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Text(uiState.question.completeCorrectedAnswer, style = MaterialTheme.typography.bodyLarge)
        
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                "Takeaway: ${uiState.question.takeaway}", 
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
        
        if (uiState.question.sourceUrls.isNotEmpty()) {
            val context = LocalContext.current
            uiState.question.sourceUrls.forEach { url ->
                TextButton(onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }) {
                    Text("Read source")
                }
            }
        }
        
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Done")
        }
    }
}

@Composable
fun AnswerCard(
    label: String?,
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showResult: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (showResult) {
        if (isCorrect) MaterialTheme.colorScheme.primary
        else if (isSelected) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.surfaceVariant
    } else {
        if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant
    }

    val containerColor = if (showResult) {
        if (isCorrect) MaterialTheme.colorScheme.surface
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !showResult, onClick = onClick),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected && !showResult) 4.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.Top
        ) {
            if (label != null) {
                Text(
                    text = "$label.",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 12.dp),
                    color = if (showResult && isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (showResult && !isCorrect) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ExplanationCard(text: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Explanation", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
