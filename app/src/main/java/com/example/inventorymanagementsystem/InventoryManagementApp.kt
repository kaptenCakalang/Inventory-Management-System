package com.example.inventorymanagementsystem

import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun InventoryManagementApp() {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Login) }
    val auth = FirebaseAuth.getInstance()

    // Check if user is already logged in
    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentScreen = AppScreen.Home
        }
    }

    when (currentScreen) {
        AppScreen.Login -> {
            LoginScreen(
                onNavigateToSurvey = {
                    currentScreen = AppScreen.Survey
                },
                onNavigateToHome = {
                    currentScreen = AppScreen.Home
                }
            )
        }
        AppScreen.Survey -> {
            SurveyScreen(
                onSurveyCompleted = {
                    currentScreen = AppScreen.Home
                }
            )
        }
        AppScreen.Home -> {
            HomeScreen(
                onLogout = {
                    auth.signOut()
                    currentScreen = AppScreen.Login
                }
            )
        }
    }
}

