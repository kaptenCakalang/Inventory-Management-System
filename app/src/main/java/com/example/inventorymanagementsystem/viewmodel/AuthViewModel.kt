package com.example.inventorymanagementsystem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementsystem.service.FirebaseAuthService
import com.example.inventorymanagementsystem.service.FirebaseFirestoreService
import com.example.inventorymanagementsystem.service.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authService = FirebaseAuthService()
    private val firestoreService = FirebaseFirestoreService()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    fun signUp(fullName: String, email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authService.signUp(email, password, fullName)
            if (result.isSuccess) {
                val user = result.getOrThrow()
                // Create user profile
                val userProfile = UserProfile(
                    userId = user.uid,
                    email = email,
                    fullName = fullName
                )
                firestoreService.saveUserProfile(userProfile)
                _authState.value = AuthState.Success(user)
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Sign up failed")
            }
        }
    }

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authService.login(email, password)
            if (result.isSuccess) {
                _authState.value = AuthState.Success(result.getOrThrow())
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun logout() {
        authService.logout()
        _authState.value = AuthState.Initial
    }

    fun resetError() {
        _authState.value = AuthState.Initial
    }
}

// Make sure AuthState is defined in the SAME FILE but OUTSIDE the AuthViewModel class
sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val user: com.google.firebase.auth.FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}