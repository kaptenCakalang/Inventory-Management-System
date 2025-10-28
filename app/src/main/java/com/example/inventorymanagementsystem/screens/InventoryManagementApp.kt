package com.example.inventorymanagementsystem.screens

import androidx.compose.runtime.*

@Composable
fun InventoryManagementApp() {
    var showLoginScreen by remember { mutableStateOf(true) }
    var showSurveyScreen by remember { mutableStateOf(false) }
    var showHomeScreen by remember { mutableStateOf(false) }

    when {
        showLoginScreen -> {
            LoginScreen(
                onNavigateToSurvey = {
                    showLoginScreen = false
                    showSurveyScreen = true
                }
            )
        }
        showSurveyScreen -> {
            SurveyScreen(
                onSurveyCompleted = {
                    showSurveyScreen = false
                    showHomeScreen = true
                }
            )
        }
        showHomeScreen -> {
            HomeScreen()
        }
    }
}