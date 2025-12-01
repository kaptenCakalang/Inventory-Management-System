package com.example.inventorymanagementsystem

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventorymanagementsystem.components.Question1
import com.example.inventorymanagementsystem.components.Question2
import com.example.inventorymanagementsystem.components.Question3
import com.example.inventorymanagementsystem.components.Question4

@Composable
fun SurveyScreen(
    modifier: Modifier = Modifier,
    onSurveyCompleted: () -> Unit = {}
) {
    var currentQuestion by remember { mutableStateOf(1) }
    var selectedFoodType by remember { mutableStateOf("") }
    var storageLocations by remember { mutableStateOf(setOf<String>()) }
    var reminderTime by remember { mutableStateOf("") }
    var appGoals by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress text - top left
        Text(
            text = "Question $currentQuestion of 4",
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Start
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (currentQuestion) {
                1 -> Question1(
                    selectedFoodType = selectedFoodType,
                    onSelectionChange = { selectedFoodType = it }
                )
                2 -> Question2(
                    selectedLocations = storageLocations,
                    onSelectionChange = { storageLocations = it }
                )
                3 -> Question3(
                    selectedTime = reminderTime,
                    onSelectionChange = { reminderTime = it }
                )
                4 -> Question4(
                    selectedGoals = appGoals,
                    onSelectionChange = { appGoals = it }
                )
            }
        }

        // Next button
        Button(
            onClick = {
                if (currentQuestion < 4) {
                    currentQuestion++
                } else {
                    onSurveyCompleted()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = when (currentQuestion) {
                1 -> selectedFoodType.isNotEmpty()
                2 -> storageLocations.size == 2
                3 -> reminderTime.isNotEmpty()
                4 -> appGoals.size == 2
                else -> false
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFAB12F),
                disabledContainerColor = Color(0xFFCCCCCC)
            )
        ) {
            Text(
                text = if (currentQuestion == 4) "Finish" else "Next",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}