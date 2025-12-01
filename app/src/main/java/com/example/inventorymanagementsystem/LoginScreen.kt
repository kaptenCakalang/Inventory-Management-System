package com.example.inventorymanagementsystem

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToSurvey: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    // State variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    // Error states
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var fullNameError by remember { mutableStateOf("") }
    var authError by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()

    // Validation functions
    fun validateEmail(): Boolean {
        return if (email.isBlank()) {
            emailError = "Email is required"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Please enter a valid email"
            false
        } else {
            emailError = ""
            true
        }
    }

    fun validatePassword(): Boolean {
        return if (password.isBlank()) {
            passwordError = "Password is required"
            false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            false
        } else {
            passwordError = ""
            true
        }
    }

    fun validateConfirmPassword(): Boolean {
        return if (!isLoginMode && password != confirmPassword) {
            confirmPasswordError = "Passwords do not match"
            false
        } else {
            confirmPasswordError = ""
            true
        }
    }

    fun validateFullName(): Boolean {
        return if (!isLoginMode && fullName.isBlank()) {
            fullNameError = "Full name is required"
            false
        } else {
            fullNameError = ""
            true
        }
    }

    fun validateForm(): Boolean {
        var isValid = validateEmail() && validatePassword()

        if (!isLoginMode) {
            isValid = isValid && validateFullName() && validateConfirmPassword()
        }

        return isValid
    }

    fun handleAuthentication() {
        if (!validateForm()) {
            return
        }

        isLoading = true
        authError = ""

        if (isLoginMode) {
            // Login
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    isLoading = false
                    if (task.isSuccessful) {
                        // Check if this is a new user (first time login)
                        val user = auth.currentUser
                        if (user != null) {
                            // For existing users, go directly to home
                            // For new users, they would go to survey
                            // We'll assume all users go to home for now
                            onNavigateToHome()
                        }
                    } else {
                        authError = task.exception?.message ?: "Login failed"
                    }
                }
        } else {
            // Sign up
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null && fullName.isNotBlank()) {
                            // Update user profile with display name
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(fullName)
                                .build()

                            user.updateProfile(profileUpdates)
                                .addOnCompleteListener { profileTask ->
                                    isLoading = false
                                    if (profileTask.isSuccessful) {
                                        // New users go to survey
                                        onNavigateToSurvey()
                                    } else {
                                        authError = profileTask.exception?.message ?: "Profile update failed"
                                    }
                                }
                        } else {
                            isLoading = false
                            onNavigateToSurvey()
                        }
                    } else {
                        isLoading = false
                        authError = task.exception?.message ?: "Registration failed"
                    }
                }
        }
    }

    fun resetErrors() {
        emailError = ""
        passwordError = ""
        confirmPasswordError = ""
        fullNameError = ""
        authError = ""
    }

    Scaffold(
        topBar = {
            if (!isLoginMode) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Create Account",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            isLoginMode = true
                            resetErrors()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            if (isLoginMode) {
                // Login Header
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Log in",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Create account",
                        fontSize = 16.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(bottom = 40.dp)
                            .clickable {
                                isLoginMode = false
                                resetErrors()
                            }
                    )
                }
            } else {
                // Sign Up Header
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            // Form Title
            Text(
                text = if (isLoginMode) "Login" else "Create Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Start
            )

            Text(
                text = if (isLoginMode) "Please sign in to continue" else "Please fill in your details",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                textAlign = TextAlign.Start
            )

            // Error message for authentication
            if (authError.isNotEmpty()) {
                Text(
                    text = authError,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Start
                )
            }

            // Loading state
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(bottom = 16.dp),
                    color = Color(0xFF4CAF50)
                )
            }

            // Form Fields
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!isLoginMode) {
                    // Full Name Field (only for sign up)
                    Column {
                        TextField(
                            value = fullName,
                            onValueChange = {
                                fullName = it
                                if (fullNameError.isNotEmpty()) validateFullName()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            placeholder = {
                                Text(
                                    text = "Full name",
                                    color = Color.Gray
                                )
                            },
                            isError = fullNameError.isNotEmpty(),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF5F5F5),
                                focusedContainerColor = Color(0xFFF5F5F5),
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                errorContainerColor = Color(0xFFFFE6E6)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        if (fullNameError.isNotEmpty()) {
                            Text(
                                text = fullNameError,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }
                }

                // Email Field
                Column {
                    TextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (emailError.isNotEmpty()) validateEmail()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text(
                                text = "Email",
                                color = Color.Gray
                            )
                        },
                        isError = emailError.isNotEmpty(),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            errorContainerColor = Color(0xFFFFE6E6)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (emailError.isNotEmpty()) {
                        Text(
                            text = emailError,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                // Password Field
                Column {
                    TextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (passwordError.isNotEmpty()) validatePassword()
                            if (confirmPasswordError.isNotEmpty()) validateConfirmPassword()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text(
                                text = "Password",
                                color = Color.Gray
                            )
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (showPassword) "Hide password" else "Show password"
                                )
                            }
                        },
                        isError = passwordError.isNotEmpty(),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            errorContainerColor = Color(0xFFFFE6E6)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (passwordError.isNotEmpty()) {
                        Text(
                            text = passwordError,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                if (!isLoginMode) {
                    // Confirm Password Field (only for sign up)
                    Column {
                        TextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                if (confirmPasswordError.isNotEmpty()) validateConfirmPassword()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            placeholder = {
                                Text(
                                    text = "Confirm Password",
                                    color = Color.Gray
                                )
                            },
                            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                    Icon(
                                        imageVector = if (showConfirmPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = if (showConfirmPassword) "Hide password" else "Show password"
                                    )
                                }
                            },
                            isError = confirmPasswordError.isNotEmpty(),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF5F5F5),
                                focusedContainerColor = Color(0xFFF5F5F5),
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                errorContainerColor = Color(0xFFFFE6E6)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        if (confirmPasswordError.isNotEmpty()) {
                            Text(
                                text = confirmPasswordError,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Forgot Password (only for login)
            if (isLoginMode) {
                Text(
                    text = "Forgot password?",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 32.dp),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action Button
            Button(
                onClick = { handleAuthentication() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text(
                    text = if (isLoginMode) "Login" else "Create Account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Switch Mode Text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isLoginMode) "Don't have an account? " else "Already have an account? ",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = if (isLoginMode) "Sign up" else "Sign in",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        isLoginMode = !isLoginMode
                        resetErrors()
                    }
                )
            }
        }
    }
}