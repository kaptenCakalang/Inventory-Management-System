package com.example.inventorymanagementsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Question1(selectedFoodType: String, onSelectionChange: (String) -> Unit) {
    val foodTypes = listOf(
        "Instant Food",
        "Canned Goods",
        "Drinks & Dairy",
        "Snack",
        "Frozen Food"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main question in #006A67
        Text(
            text = "A few quick questions :\n\nWhat kind of food do you usually store?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            color = Color(0xFF006A67), // #006A67 for main question
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
        )

        QuestionOptionsLayout(
            options = foodTypes,
            selectedOption = selectedFoodType,
            onOptionSelected = onSelectionChange,
            singleSelection = true
        )
    }
}

@Composable
fun Question2(selectedLocations: Set<String>, onSelectionChange: (Set<String>) -> Unit) {
    val locations = listOf("Fridge", "Freezer", "Pantry")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main question in #006A67
        Text(
            text = "Where do you usually store your food?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            color = Color(0xFF006A67), // #006A67 for main question
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Supporting text in #FAB12F
        Text(
            text = "Choose two below!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            color = Color(0xFFFAB12F), // #FAB12F for supporting text
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        QuestionOptionsLayout(
            options = locations,
            selectedOptions = selectedLocations,
            onOptionsSelected = onSelectionChange,
            maxSelections = 2
        )
    }
}

@Composable
fun Question3(selectedTime: String, onSelectionChange: (String) -> Unit) {
    val times = listOf("Morning", "Afternoon", "Evening", "Night")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main question in #006A67
        Text(
            text = "When do you prefer to get expiry reminders?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            color = Color(0xFF006A67), // #006A67 for main question
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Supporting text in #FAB12F
        Text(
            text = "Choose one below!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            color = Color(0xFFFAB12F), // #FAB12F for supporting text
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        QuestionOptionsLayout(
            options = times,
            selectedOption = selectedTime,
            onOptionSelected = onSelectionChange,
            singleSelection = true
        )
    }
}

@Composable
fun Question4(selectedGoals: Set<String>, onSelectionChange: (Set<String>) -> Unit) {
    val goals = listOf(
        "Get Expiry Reminders",
        "Reduce Food Waste",
        "Save Money",
        "Use Food Before It Goes Bad"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main question in #006A67
        Text(
            text = "What's your goal with this app?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            color = Color(0xFF006A67), // #006A67 for main question
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Supporting text in #FAB12F
        Text(
            text = "Choose two below!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            color = Color(0xFFFAB12F), // #FAB12F for supporting text
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        QuestionOptionsLayout(
            options = goals,
            selectedOptions = selectedGoals,
            onOptionsSelected = onSelectionChange,
            maxSelections = 2
        )
    }
}


@Composable
fun QuestionOptionsLayout(
    options: List<String>,
    selectedOption: String = "",
    selectedOptions: Set<String> = setOf(),
    onOptionSelected: (String) -> Unit = {},
    onOptionsSelected: (Set<String>) -> Unit = {},
    singleSelection: Boolean = false,
    maxSelections: Int = 1
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEach { option ->
            val isSelected = if (singleSelection) {
                option == selectedOption
            } else {
                selectedOptions.contains(option)
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            if (singleSelection) {
                                onOptionSelected(if (isSelected) "" else option)
                            } else {
                                val newSelection = selectedOptions.toMutableSet()
                                if (isSelected) {
                                    newSelection.remove(option)
                                } else if (newSelection.size < maxSelections) {
                                    newSelection.add(option)
                                }
                                onOptionsSelected(newSelection)
                            }
                        }
                    ),
                shape = MaterialTheme.shapes.medium,
                color = if (isSelected) Color(0xFFE8F5E9) else Color.White,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) Color(0xFFFAB12F) else Color(0xFFDDDDDD)
                ),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(20.dp).padding(end = 12.dp)) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        color = Color(0xFFFAB12F),
                                        shape = MaterialTheme.shapes.extraSmall
                                    )
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(Color.White, MaterialTheme.shapes.extraSmall)
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFF999999),
                                        shape = MaterialTheme.shapes.extraSmall
                                    )
                            )
                        }
                    }
                    Text(
                        text = option,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionLayout(
    title: String,
    options: List<String>,
    selectedOption: String = "",
    selectedOptions: Set<String> = setOf(),
    onOptionSelected: (String) -> Unit = {},
    onOptionsSelected: (Set<String>) -> Unit = {},
    singleSelection: Boolean = false,
    maxSelections: Int = 1
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            color = Color(0xFF006A67), // CHANGED: Main question text to #006A67
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            options.forEach { option ->
                val isSelected = if (singleSelection) {
                    option == selectedOption
                } else {
                    selectedOptions.contains(option)
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = isSelected,
                            onClick = {
                                if (singleSelection) {
                                    onOptionSelected(if (isSelected) "" else option)
                                } else {
                                    val newSelection = selectedOptions.toMutableSet()
                                    if (isSelected) {
                                        newSelection.remove(option)
                                    } else if (newSelection.size < maxSelections) {
                                        newSelection.add(option)
                                    }
                                    onOptionsSelected(newSelection)
                                }
                            }
                        ),
                    shape = MaterialTheme.shapes.medium,
                    color = if (isSelected) Color(0xFFE8F5E9) else Color.White,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) Color(0xFFFAB12F) else Color(0xFFDDDDDD)
                    ),
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(20.dp).padding(end = 12.dp)) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(
                                            color = Color(0xFFFAB12F),
                                            shape = MaterialTheme.shapes.extraSmall
                                        )
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(Color.White, MaterialTheme.shapes.extraSmall)
                                        .border(
                                            width = 1.dp,
                                            color = Color(0xFF999999),
                                            shape = MaterialTheme.shapes.extraSmall
                                        )
                                )
                            }
                        }
                        Text(
                            text = option,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}