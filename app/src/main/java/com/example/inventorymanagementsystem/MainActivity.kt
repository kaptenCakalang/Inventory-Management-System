package com.example.inventorymanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.inventorymanagementsystem.ui.theme.TemplateTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemplateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FoodlyApp()
                }
            }
        }
    }
}

@Composable
fun FoodlyApp() {
    val auth = FirebaseAuth.getInstance()
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Login) }

    // Check if user is already logged in
    LaunchedEffect(key1 = auth.currentUser) {
        if (auth.currentUser != null) {
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

sealed class AppScreen {
    object Login : AppScreen()
    object Survey : AppScreen()
    object Home : AppScreen()
}