package com.example.inventorymanagementsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CameraAlt

import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.layout.width

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import android.app.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.derivedStateOf

// Food status enum
enum class FoodStatus {
    EXPIRED, EXPIRING_SOON, FRESH
}

data class AvailableIngredient(
    val quantity: Double,
    val unit: String
)

// Recipe Ingredient data class
data class RecipeIngredient(
    val name: String = "",
    val quantity: Double = 1.0,
    val unit: String = "pcs"
)

// FoodItem data class
data class FoodItem(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val quantity: Double = 1.0, // NEW: Default quantity
    val unit: String = "pcs", // NEW: Unit type
    val expiryDate: String = "",
    val storage: String = "",
    val additionalNotes: String = "",
    val status: FoodStatus = FoodStatus.FRESH,
    val statusText: String = ""
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "name" to name,
            "quantity" to quantity,
            "unit" to unit,
            "expiryDate" to expiryDate,
            "storage" to storage,
            "additionalNotes" to additionalNotes,
            "status" to status.name,
            "statusText" to statusText
        )
    }

    companion object {
        fun fromMap(id: String, data: Map<String, Any>): FoodItem {
            return FoodItem(
                id = id,
                userId = data["userId"] as? String ?: "",
                name = data["name"] as? String ?: "",
                quantity = (data["quantity"] as? Double) ?: 1.0,
                unit = data["unit"] as? String ?: "pcs",
                expiryDate = data["expiryDate"] as? String ?: "",
                storage = data["storage"] as? String ?: "",
                additionalNotes = data["additionalNotes"] as? String ?: "",
                status = when (data["status"] as? String) {
                    "EXPIRED" -> FoodStatus.EXPIRED
                    "EXPIRING_SOON" -> FoodStatus.EXPIRING_SOON
                    else -> FoodStatus.FRESH
                },
                statusText = data["statusText"] as? String ?: ""
            )
        }
    }
}

data class Recipe(
    val id: String = "",
    val name: String = "",
    val ingredients: List<RecipeIngredient> = emptyList(),
    val instructions: String = "",
    val imageUrl: String = ""
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "ingredients" to ingredients.map { ingredient ->
                mapOf(
                    "name" to ingredient.name,
                    "quantity" to ingredient.quantity,
                    "unit" to ingredient.unit
                )
            },
            "instructions" to instructions,
            "imageUrl" to imageUrl
        )
    }

    companion object {
        fun fromMap(id: String, data: Map<String, Any>): Recipe {
            val ingredientsList = mutableListOf<RecipeIngredient>()

            try {
                when (val ingredientsData = data["ingredients"]) {
                    is List<*> -> {
                        ingredientsData.forEach { ingredient ->
                            when (ingredient) {
                                is Map<*, *> -> {
                                    val name = ingredient["name"] as? String ?: ""
                                    val quantity = when (val q = ingredient["quantity"]) {
                                        is Double -> q
                                        is Long -> q.toDouble()
                                        is Int -> q.toDouble()
                                        else -> 1.0
                                    }
                                    val unit = ingredient["unit"] as? String ?: "pcs"

                                    if (name.isNotBlank()) {
                                        ingredientsList.add(RecipeIngredient(name, quantity, unit))
                                    }
                                }
                                is String -> {
                                    // For backward compatibility with old string-only ingredients
                                    ingredientsList.add(RecipeIngredient(ingredient, 1.0, "pcs"))
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error parsing ingredients: $e")
            }

            return Recipe(
                id = id,
                name = data["name"] as? String ?: "",
                ingredients = ingredientsList,
                instructions = data["instructions"] as? String ?: "",
                imageUrl = data["imageUrl"] as? String ?: ""
            )
        }
    }
}

@Composable
fun ExpandableFAB(
    onMainClick: () -> Unit,
    onKeyboardClick: () -> Unit,
    onScanClick: () -> Unit,
    onMicrophoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // Show extra buttons only when expanded
        if (expanded) {
            // Microphone button (top-left)
            FloatingActionButton(
                onClick = {
                    expanded = false
                    onMicrophoneClick()
                },
                containerColor = Color(0xFF006A67),
                contentColor = Color.White,
                modifier = Modifier
                    .size(38.dp)
                    .align(Alignment.TopStart)
                    .offset(x = (-32).dp, y = (-36).dp),
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Mic, "Voice Input")
            }

            // Scan button (top)
            FloatingActionButton(
                onClick = {
                    expanded = false
                    onScanClick()
                },
                containerColor = Color(0xFF006A67),
                contentColor = Color.White,
                modifier = Modifier
                    .size(38.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = (-52).dp),
                shape = CircleShape
            ) {
                Icon(Icons.Filled.CameraAlt, "Scan Item")
            }

            // Keyboard button (top-right)
            FloatingActionButton(
                onClick = {
                    expanded = false
                    onKeyboardClick()
                },
                containerColor = Color(0xFF006A67),
                contentColor = Color.White,
                modifier = Modifier
                    .size(38.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 32.dp, y = (-36).dp),
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Keyboard, "Manual Entry")
            }
        }

        // Main FAB (plus/close button)
        FloatingActionButton(
            onClick = {
                expanded = !expanded
            },
            containerColor = Color(0xFFFF9800),
            contentColor = Color.White,
            modifier = Modifier.size(56.dp),
            shape = CircleShape
        ) {
            AnimatedContent(targetState = expanded, label = "fab_icon") { isExpanded ->
                if (isExpanded) {
                    Icon(Icons.Filled.Close, "Close Menu")
                } else {
                    Icon(Icons.Filled.Add, "Add Item")
                }
            }
        }
    }
}

// Helper function to parse date and determine status
private fun getFoodStatus(expiryDateStr: String): Pair<FoodStatus, String> {
    if (expiryDateStr.isBlank()) {
        return FoodStatus.FRESH to "Fresh"
    }

    val soonThreshold = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, 3)
    }.time

    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    val expiryDate = try {
        SimpleDateFormat("d/M/yyyy", Locale.getDefault()).parse(expiryDateStr)
    } catch (e: Exception) {
        return FoodStatus.FRESH to "Fresh"
    }

    return when {
        expiryDate.before(today) -> FoodStatus.EXPIRED to "Expired on $expiryDateStr"
        expiryDate.before(soonThreshold) -> FoodStatus.EXPIRING_SOON to "Expires on $expiryDateStr"
        else -> FoodStatus.FRESH to "Expires on $expiryDateStr"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualEntryScreen(
    onBackClick: () -> Unit,
    onAddItem: (String, Double, String, String, String, String) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var selectedUnit by remember { mutableStateOf("pcs") }
    var expiryDate by remember { mutableStateOf("") }
    var selectedStorage by remember { mutableStateOf("Fridge") }
    var notes by remember { mutableStateOf("") }

    val isFormValid by remember(itemName, quantity) {
        mutableStateOf(itemName.isNotBlank() && quantity.toDoubleOrNull() != null)
    }

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            expiryDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.minDate = System.currentTimeMillis()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Item Manually") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(bottom = 90.dp)
                    .width(140.dp)
                    .height(56.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        if (isFormValid) {
                            val quantityValue = quantity.toDoubleOrNull() ?: 1.0
                            onAddItem(itemName, quantityValue, selectedUnit, expiryDate, selectedStorage, notes)
                            onBackClick()
                        }
                    },
                    containerColor = if (isFormValid) Color(0xFFFF9800) else Color(0xFFCCCCCC),
                    contentColor = Color.White,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Item")
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .background(Color.White)
                .padding(24.dp)
        ) {
            // Item Name Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Enter Item Name",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text(text = "Enter item name", color = Color.Gray)
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Text(
                        text = "${itemName.length}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                    )
                }
                Text(
                    text = "Select",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quantity and Unit Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quantity Field
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Quantity",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    TextField(
                        value = quantity,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.toDoubleOrNull() != null) {
                                quantity = newValue
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text(text = "0", color = Color.Gray)
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                // Unit Dropdown
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Unit",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    var expanded by remember { mutableStateOf(false) }
                    val unitOptions = listOf("pcs", "kg", "g", "L", "mL", "oz", "lb", "cup", "tbsp", "tsp")

                    Box(modifier = Modifier.fillMaxWidth()) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                value = selectedUnit,
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .menuAnchor(),
                                placeholder = {
                                    Text(text = "Select unit", color = Color.Gray)
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color(0xFFF5F5F5),
                                    focusedContainerColor = Color(0xFFF5F5F5),
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp),
                                readOnly = true
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                unitOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = option, fontSize = 16.sp)
                                        },
                                        onClick = {
                                            selectedUnit = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Expiry Date Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Expiration Date",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box {
                    TextField(
                        value = expiryDate,
                        onValueChange = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text(text = "DD/MM/YYYY", color = Color.Gray)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Calendar Icon"
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        readOnly = true,
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Transparent)
                            .clickable { showDatePicker = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Storage Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Storage",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                var expanded by remember { mutableStateOf(false) }
                val storageOptions = listOf("Fridge", "Freezer", "Pantry")

                Box(modifier = Modifier.fillMaxWidth()) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = selectedStorage,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .menuAnchor(),
                            placeholder = {
                                Text(text = "Select", color = Color.Gray)
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF5F5F5),
                                focusedContainerColor = Color(0xFFF5F5F5),
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp),
                            readOnly = true
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            storageOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = option, fontSize = 16.sp)
                                    },
                                    onClick = {
                                        selectedStorage = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Additional Notes Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Additional Notes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = {
                        Text(text = "Add any additional notes here...", color = Color.Gray)
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        if (showDatePicker) {
            datePickerDialog.setOnDismissListener {
                showDatePicker = false
            }
            datePickerDialog.show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit = {}
) {
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    var selectedBottomNavItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }
    val scope = rememberCoroutineScope()

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userName = currentUser?.displayName ?: "User"
    val userInitial = currentUser?.displayName?.firstOrNull()?.toString() ?: "U"

    // Stateful food items for each storage section
    val fridgeItems = remember { mutableStateListOf<FoodItem>() }
    val freezerItems = remember { mutableStateListOf<FoodItem>() }
    val pantryItems = remember { mutableStateListOf<FoodItem>() }

    // Recipes state
    val allRecipes = remember { mutableStateListOf<Recipe>() }
    val cookableRecipes = remember { mutableStateListOf<Recipe>() }

    // Track when recipes are loaded
    var recipesLoaded by remember { mutableStateOf(false) }

    // Available ingredients with quantities
    val availableIngredients by remember(fridgeItems, freezerItems, pantryItems) {
        derivedStateOf {
            val allItems = fridgeItems + freezerItems + pantryItems
            allItems.groupBy { it.name.toLowerCase(Locale.getDefault()) }
                .mapValues { (_, items) ->
                    AvailableIngredient(
                        quantity = items.sumByDouble { it.quantity },
                        unit = items.firstOrNull()?.unit ?: "pcs"
                    )
                }
        }
    }

    // Recipe matching logic
    LaunchedEffect(availableIngredients, recipesLoaded) {
        if (!recipesLoaded || availableIngredients.isEmpty()) {
            return@LaunchedEffect
        }

        println("DEBUG: Recipe matching triggered")
        println("DEBUG: Available ingredients with quantities: $availableIngredients")

        val matchingRecipes = allRecipes.filter { recipe ->
            val canMake = recipe.ingredients.all { requiredIngredient ->
                val availableIngredient = availableIngredients[requiredIngredient.name.toLowerCase(Locale.getDefault())]
                if (availableIngredient != null) {
                    // Check if units match and quantity is sufficient
                    availableIngredient.unit.equals(requiredIngredient.unit, ignoreCase = true) &&
                            availableIngredient.quantity >= requiredIngredient.quantity
                } else {
                    false
                }
            }
            println("DEBUG: Recipe '${recipe.name}' can be made: $canMake")
            canMake
        }

        cookableRecipes.clear()
        cookableRecipes.addAll(matchingRecipes)
        println("DEBUG: Final cookable recipes: ${cookableRecipes.size}")
    }

    // Load recipes
    // Update the recipe loading in the LaunchedEffect:
    LaunchedEffect(Unit) {
        addDefaultRecipes()
        val db = FirebaseFirestore.getInstance()
        db.collection("recipes")
            .get()
            .addOnSuccessListener { documents ->
                val recipeList = mutableListOf<Recipe>()
                val seenRecipes = mutableSetOf<String>()

                documents.forEach { doc ->
                    try {
                        val data = doc.data
                        val name = data["name"] as? String ?: ""

                        // Parse ingredients
                        val ingredientsList = mutableListOf<RecipeIngredient>()
                        val ingredientsData = data["ingredients"] as? List<Map<String, Any>> ?: emptyList()
                        ingredientsData.forEach { ingredientMap ->
                            val ingredientName = ingredientMap["name"] as? String ?: ""
                            val ingredientQuantity = (ingredientMap["quantity"] as? Double) ?:
                            (ingredientMap["quantity"] as? Long)?.toDouble() ?: 1.0
                            val ingredientUnit = ingredientMap["unit"] as? String ?: "pcs"

                            if (ingredientName.isNotBlank()) {
                                ingredientsList.add(RecipeIngredient(ingredientName, ingredientQuantity, ingredientUnit))
                            }
                        }

                        val instructions = data["instructions"] as? String ?: ""
                        val imageUrl = data["imageUrl"] as? String ?: ""

                        if (name.isNotBlank() && ingredientsList.isNotEmpty() && !seenRecipes.contains(name)) {
                            seenRecipes.add(name)
                            recipeList.add(Recipe(doc.id, name, ingredientsList, instructions, imageUrl))
                            println("DEBUG: Loaded recipe: $name with ${ingredientsList.size} ingredients")
                        }
                    } catch (e: Exception) {
                        println("DEBUG: Error parsing recipe ${doc.id}: $e")
                    }
                }

                allRecipes.clear()
                allRecipes.addAll(recipeList)
                recipesLoaded = true
                println("DEBUG: Successfully loaded ${recipeList.size} unique recipes")
            }
            .addOnFailureListener { e ->
                println("DEBUG: Error loading recipes: $e")
            }
    }

    // Load items from Firebase
    DisposableEffect(key1 = currentUser?.uid) {
        if (currentUser?.uid == null) {
            onDispose { }
        } else {
            val db = FirebaseFirestore.getInstance()
            val listenerRegistration = db.collection("foodItems")
                .whereEqualTo("userId", currentUser.uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        println("Listen failed: $error")
                        return@addSnapshotListener
                    }
                    if (snapshot == null) return@addSnapshotListener

                    val tempFridgeItems = mutableListOf<FoodItem>()
                    val tempFreezerItems = mutableListOf<FoodItem>()
                    val tempPantryItems = mutableListOf<FoodItem>()

                    snapshot.documents.forEach { doc ->
                        doc.data?.let { data ->
                            val foodItem = FoodItem.fromMap(doc.id, data)
                            when (foodItem.storage) {
                                "Fridge" -> tempFridgeItems.add(foodItem)
                                "Freezer" -> tempFreezerItems.add(foodItem)
                                "Pantry" -> tempPantryItems.add(foodItem)
                            }
                        }
                    }

                    scope.launch {
                        fridgeItems.clear()
                        fridgeItems.addAll(tempFridgeItems)
                        freezerItems.clear()
                        freezerItems.addAll(tempFreezerItems)
                        pantryItems.clear()
                        pantryItems.addAll(tempPantryItems)

                        println("DEBUG: Updated inventory - Fridge: ${fridgeItems.size}, Freezer: ${freezerItems.size}, Pantry: ${pantryItems.size}")
                    }
                }

            onDispose {
                listenerRegistration.remove()
            }
        }
    }

    // Function to handle adding items
    fun handleAddItem(itemName: String, quantity: Double, unit: String, expiryDate: String, storage: String, notes: String) {
        val userId = currentUser?.uid ?: return
        val (status, statusText) = getFoodStatus(expiryDate)

        if (status == FoodStatus.EXPIRED) {
            println("Cannot add an item that is already expired.")
            return
        }

        val newItem = FoodItem(
            userId = userId,
            name = itemName,
            quantity = quantity,
            unit = unit,
            expiryDate = expiryDate,
            storage = storage,
            additionalNotes = notes,
            status = status,
            statusText = statusText
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("foodItems")
            .add(newItem.toMap())
            .addOnSuccessListener {
                println("Item added successfully!")
            }
            .addOnFailureListener { e ->
                println("Error saving item: ${e.message}")
            }
    }

    fun handleDeleteItem(item: FoodItem) {
        val itemId = item.id
        if (itemId.isBlank()) {
            println("Error: Item ID is blank, cannot delete.")
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("foodItems").document(itemId)
            .delete()
            .addOnSuccessListener {
                println("DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { e ->
                println("Error deleting document: $e")
            }
    }

    // Function to handle making a recipe
    fun handleMakeRecipe(recipe: Recipe) {
        val userId = currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("foodItems")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val batch = db.batch()
                val updates = mutableListOf<Pair<String, Double>>() // itemId to newQuantity
                val deletions = mutableListOf<String>() // itemIds to delete

                recipe.ingredients.forEach { requiredIngredient ->
                    var remainingQuantity = requiredIngredient.quantity

                    // Find matching items (same name and unit)
                    val matchingItems = snapshot.documents.filter { doc ->
                        val item = FoodItem.fromMap(doc.id, doc.data ?: emptyMap())
                        item.name.equals(requiredIngredient.name, ignoreCase = true) &&
                                item.unit.equals(requiredIngredient.unit, ignoreCase = true)
                    }.sortedBy { it.id } // Sort for consistency

                    for (document in matchingItems) {
                        if (remainingQuantity <= 0) break

                        val item = FoodItem.fromMap(document.id, document.data ?: emptyMap())

                        if (item.quantity > remainingQuantity) {
                            // Update the item with reduced quantity
                            val newQuantity = item.quantity - remainingQuantity
                            updates.add(document.id to newQuantity)
                            remainingQuantity = 0.0
                        } else {
                            // Remove the entire item
                            remainingQuantity -= item.quantity
                            deletions.add(document.id)
                        }
                    }

                    if (remainingQuantity > 0) {
                        println("Warning: Not enough ${requiredIngredient.name} to make the recipe")
                    }
                }

                // Apply all updates and deletions
                updates.forEach { (itemId, newQuantity) ->
                    val docRef = db.collection("foodItems").document(itemId)
                    batch.update(docRef, "quantity", newQuantity)
                }

                deletions.forEach { itemId ->
                    val docRef = db.collection("foodItems").document(itemId)
                    batch.delete(docRef)
                }

                batch.commit()
                    .addOnSuccessListener {
                        println("Recipe made successfully!")
                        // Show success message (you could add a snackbar here)
                    }
                    .addOnFailureListener { e ->
                        println("Error making recipe: $e")
                    }
            }
            .addOnFailureListener { e ->
                println("Error fetching items: $e")
            }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // User profile section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // User avatar
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userInitial,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // User name
                    Text(
                        text = "Hi, $userName",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Divider()

                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        selectedBottomNavItem = BottomNavItem.Settings
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        onLogout()
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                if (selectedRecipe == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bottom_nav_background),
                            contentDescription = "Bottom Navigation Background",
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.BottomCenter),
                            contentScale = ContentScale.FillBounds
                        )

                        NavigationBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(Color.Transparent),
                            containerColor = Color.Transparent
                        ) {
                            val navItems = listOf(
                                BottomNavItem.Home,
                                BottomNavItem.Pantry,
                                BottomNavItem.Recipe,
                                BottomNavItem.Reward
                            )

                            navItems.take(2).forEach { item ->
                                NavigationBarItem(
                                    selected = selectedBottomNavItem == item,
                                    onClick = { selectedBottomNavItem = item },
                                    icon = {
                                        Icon(
                                            imageVector = if (selectedBottomNavItem == item) {
                                                item.selectedIcon
                                            } else {
                                                item.unselectedIcon
                                            },
                                            contentDescription = item.title,
                                            tint = if (selectedBottomNavItem == item) {
                                                Color(0xFF333333)
                                            } else {
                                                Color.White
                                            }
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            color = if (selectedBottomNavItem == item) {
                                                Color(0xFF333333)
                                            } else {
                                                Color.White
                                            }
                                        )
                                    }
                                )
                            }

                            NavigationBarItem(
                                selected = false,
                                onClick = {},
                                icon = {},
                                label = {}
                            )

                            navItems.takeLast(2).forEach { item ->
                                NavigationBarItem(
                                    selected = selectedBottomNavItem == item,
                                    onClick = { selectedBottomNavItem = item },
                                    icon = {
                                        Icon(
                                            imageVector = if (selectedBottomNavItem == item) {
                                                item.selectedIcon
                                            } else {
                                                item.unselectedIcon
                                            },
                                            contentDescription = item.title,
                                            tint = if (selectedBottomNavItem == item) {
                                                Color(0xFF333333)
                                            } else {
                                                Color.White
                                            }
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            color = if (selectedBottomNavItem == item) {
                                                Color(0xFF333333)
                                            } else {
                                                Color.White
                                            }
                                        )
                                    }
                                )
                            }
                        }

                        ExpandableFAB(
                            onMainClick = { /* handled internally */ },
                            onKeyboardClick = {
                                selectedBottomNavItem = BottomNavItem.Add
                            },
                            onScanClick = {
                                // Show scan screen or dialog
                            },
                            onMicrophoneClick = {
                                // Show voice input screen or dialog
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .align(Alignment.TopCenter)
                                .offset(y = (-16).dp)
                        )
                    }
                }
            }
        ) { innerPadding ->
            if (selectedRecipe != null) {
                RecipeDetailScreen(
                    recipe = selectedRecipe!!,
                    onBackClick = { selectedRecipe = null },
                    onMakeRecipe = { recipe ->
                        handleMakeRecipe(recipe)
                        selectedRecipe = null // Go back to recipe list
                    }
                )
            } else {
                when (selectedBottomNavItem) {
                    is BottomNavItem.Home -> HomeDashboardContent(
                        modifier = Modifier.padding(innerPadding),
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        },
                        fridgeItems = fridgeItems,
                        freezerItems = freezerItems,
                        pantryItems = pantryItems,
                        onDeleteItem = { itemToDelete ->
                            handleDeleteItem(itemToDelete)
                        },
                        userName = userName
                    )
                    is BottomNavItem.Pantry -> PantryContent(
                        modifier = Modifier.padding(innerPadding),
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        },
                        fridgeItems = fridgeItems,
                        freezerItems = freezerItems,
                        pantryItems = pantryItems,
                        onDeleteItem = { item -> handleDeleteItem(item) },
                        onFridgeItemsChange = { fridgeItems.clear(); fridgeItems.addAll(it) },
                        onFreezerItemsChange = { freezerItems.clear(); freezerItems.addAll(it) },
                        onPantryItemsChange = { pantryItems.clear(); pantryItems.addAll(it) }
                    )
                    is BottomNavItem.Recipe -> RecipeContent(
                        recipes = cookableRecipes,
                        onRecipeClick = { recipe ->
                            selectedRecipe = recipe
                        }
                    )
                    is BottomNavItem.Reward -> ComingSoonContent("Reward")
                    is BottomNavItem.Settings -> SettingsContent()
                    is BottomNavItem.Add -> ManualEntryScreen(
                        onBackClick = {
                            selectedBottomNavItem = BottomNavItem.Home
                        },
                        onAddItem = { itemName, quantity, unit, expiryDate, storage, notes ->
                            handleAddItem(itemName, quantity, unit, expiryDate, storage, notes)
                        }
                    )
                }
            }
        }
    }
}

private fun addDefaultRecipes() {
    val db = FirebaseFirestore.getInstance()
    val recipesCollection = db.collection("recipes")

    // 16 new recipes with quantities and units
    val defaultRecipes = listOf(
        hashMapOf(
            "name" to "Avocado Toast",
            "ingredients" to arrayListOf(
                hashMapOf("name" to "bread", "quantity" to 2.0, "unit" to "slices"),
                hashMapOf("name" to "avocado", "quantity" to 1.0, "unit" to "pcs"),
                hashMapOf("name" to "lemon juice", "quantity" to 1.0, "unit" to "tbsp"),
                hashMapOf("name" to "salt", "quantity" to 0.5, "unit" to "tsp")
            ),
            "instructions" to "1. Toast bread until golden\n2. Mash avocado with lemon juice and salt\n3. Spread on toast and enjoy",
            "imageUrl" to "https://drive.google.com/uc?export=view&id=19wlUWnfWU3gNd6w9cCNKEbqYFzZJ8RdB"
        ),
        hashMapOf(
            "name" to "Banana Smoothie",
            "ingredients" to arrayListOf(
                hashMapOf("name" to "banana", "quantity" to 2.0, "unit" to "pcs"),
                hashMapOf("name" to "milk", "quantity" to 300.0, "unit" to "mL"),
                hashMapOf("name" to "honey", "quantity" to 1.0, "unit" to "tbsp"),
                hashMapOf("name" to "ice cubes", "quantity" to 5.0, "unit" to "pcs")
            ),
            "instructions" to "1. Blend all ingredients until smooth\n2. Pour into glass and serve immediately",
            "imageUrl" to "https://drive.google.com/uc?export=view&id=1fz24lpxjskFXj6KD9jRxyTNAFwNbVJCy"
        ),
        hashMapOf(
            "name" to "Vegetable Stir Fry",
            "ingredients" to arrayListOf(
                hashMapOf("name" to "bell pepper", "quantity" to 1.0, "unit" to "pcs"),
                hashMapOf("name" to "carrot", "quantity" to 2.0, "unit" to "pcs"),
                hashMapOf("name" to "broccoli", "quantity" to 100.0, "unit" to "g"),
                hashMapOf("name" to "soy sauce", "quantity" to 2.0, "unit" to "tbsp"),
                hashMapOf("name" to "garlic", "quantity" to 2.0, "unit" to "cloves")
            ),
            "instructions" to "1. Chop vegetables\n2. Stir-fry in oil with garlic\n3. Add soy sauce and cook for 5 minutes",
            "imageUrl" to "https://drive.google.com/uc?export=view&id=1fTaOqnBdwkx0-cpy78uzmhmRXDkEUOuQ"
        ),
        hashMapOf(
            "name" to "Tomato Pasta",
            "ingredients" to arrayListOf(
                hashMapOf("name" to "pasta", "quantity" to 200.0, "unit" to "g"),
                hashMapOf("name" to "tomato", "quantity" to 4.0, "unit" to "pcs"),
                hashMapOf("name" to "garlic", "quantity" to 3.0, "unit" to "cloves"),
                hashMapOf("name" to "basil", "quantity" to 10.0, "unit" to "g"),
                hashMapOf("name" to "olive oil", "quantity" to 2.0, "unit" to "tbsp")
            ),
            "instructions" to "1. Cook pasta according to package\n2. Sauté garlic, add chopped tomatoes\n3. Combine with pasta and basil",
            "imageUrl" to "https://drive.google.com/uc?export=view&id=1EeQT5aAR7UMBqnIxrU20V0uNduqwn8yl"
        ),
        hashMapOf(
            "name" to "Tuna Salad",
            "ingredients" to arrayListOf(
                hashMapOf("name" to "tuna", "quantity" to 1.0, "unit" to "can"),
                hashMapOf("name" to "mayonnaise", "quantity" to 2.0, "unit" to "tbsp"),
                hashMapOf("name" to "celery", "quantity" to 1.0, "unit" to "stalk"),
                hashMapOf("name" to "onion", "quantity" to 0.25, "unit" to "pcs"),
                hashMapOf("name" to "lemon juice", "quantity" to 1.0, "unit" to "tsp")
            ),
            "instructions" to "1. Drain tuna\n2. Mix with chopped celery, onion, mayo, and lemon juice\n3. Serve on bread or crackers",
            "imageUrl" to "https://drive.google.com/uc?export=view&id=147K0SUCRAVXjW5AqC5m9eTYUTytcECkU"
        ),
        hashMapOf(
            "name" to "Fried Rice",
            "ingredients" to arrayListOf(
                hashMapOf("name" to "rice", "quantity" to 200.0, "unit" to "g"),
                hashMapOf("name" to "egg", "quantity" to 2.0, "unit" to "pcs"),
                hashMapOf("name" to "carrot", "quantity" to 1.0, "unit" to "pcs"),
                hashMapOf("name" to "pea", "quantity" to 100.0, "unit" to "g"),
                hashMapOf("name" to "soy sauce", "quantity" to 2.0, "unit" to "tbsp")
            ),
            "instructions" to "1. Cook rice and let cool\n2. Scramble eggs, set aside\n3. Stir-fry vegetables, add rice, eggs, and soy sauce",
            "imageUrl" to "https://drive.google.com/uc?export=view&id=1Md8PGTWaN5C5G_6ItaHJNdCuykuWx8d8"
        )
    )

    defaultRecipes.forEach { recipeData ->
        recipesCollection
            .whereEqualTo("name", recipeData["name"])
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    recipesCollection.add(recipeData)
                        .addOnSuccessListener {
                            println("Successfully added recipe: ${recipeData["name"]}")
                        }
                        .addOnFailureListener { e ->
                            println("Error adding recipe: $e")
                        }
                }
            }
    }
}

@Composable
fun ComingSoonContent(pageName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Build,
            contentDescription = "Coming Soon",
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF4CAF50)
        )
        Text(
            text = "$pageName Coming Soon",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            text = "This feature is under development",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
fun HomeDashboardContent(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    fridgeItems: List<FoodItem>,
    freezerItems: List<FoodItem>,
    pantryItems: List<FoodItem>,
    onDeleteItem: (FoodItem) -> Unit,
    userName: String
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Expired", "Expiring Soon", "Fresh")

    val allItems = fridgeItems + freezerItems + pantryItems
    val expiredItems = allItems.filter { it.status == FoodStatus.EXPIRED }
    val expiringSoonItems = allItems.filter { it.status == FoodStatus.EXPIRING_SOON }
    val freshItems = allItems.filter { it.status == FoodStatus.FRESH }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Fixed top section
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome section with avatar, text, and menu button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.firstOrNull()?.toString() ?: "U",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Text Column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Hi, $userName",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // Navigation drawer button
                IconButton(
                    onClick = onMenuClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Navigation Menu",
                        tint = Color(0xFF4CAF50)
                    )
                }
            }

            // Notification card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Your Milk is almost up!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Try a milkshake, pancakes, or a creamy latte?",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Food stock section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Check Your Food Stock!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Tuesday, 9 September",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            // Clickable navigation tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                tabs.forEachIndexed { index, tab ->
                    StockStatusItem(
                        count = when (index) {
                            0 -> expiredItems.size
                            1 -> expiringSoonItems.size
                            else -> freshItems.size
                        },
                        label = tab,
                        color = when (index) {
                            0 -> Color(0xFFF44336)
                            1 -> Color(0xFFFF9800)
                            else -> Color(0xFF4CAF50)
                        },
                        isSelected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }

        // Food list section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F9FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            when (selectedTab) {
                0 -> FoodItemsColumnWithDelete(
                    items = expiredItems,
                    onDeleteItem = onDeleteItem
                )
                1 -> FoodItemsColumnWithDelete(
                    items = expiringSoonItems,
                    onDeleteItem = onDeleteItem
                )
                2 -> FoodItemsColumnWithDelete(
                    items = freshItems,
                    onDeleteItem = onDeleteItem
                )
            }
        }
    }
}

@Composable
fun FoodItemsColumnWithDelete(
    items: List<FoodItem>,
    onDeleteItem: (FoodItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            FoodItemRowWithDelete(
                item = item,
                onDelete = { onDeleteItem(item) }
            )
        }
    }
}

@Composable
fun FoodItemRowWithDelete(
    item: FoodItem,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "${item.quantity} ${item.unit}",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = item.statusText,
                    fontSize = 14.sp,
                    color = when (item.status) {
                        FoodStatus.EXPIRED -> Color(0xFFF44336)
                        FoodStatus.EXPIRING_SOON -> Color(0xFFFF9800)
                        FoodStatus.FRESH -> Color(0xFF4CAF50)
                        else -> Color.Gray
                    }
                )
            }

            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFF44336)
                )
            }
        }
    }
}

@Composable
fun StockStatusItem(
    count: Int,
    label: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) color else Color(0xFFF5F5F5)
    val textColor = if (isSelected) Color.White else color

    Card(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = count.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Bottom navigation items
sealed class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : BottomNavItem("Home", Icons.Outlined.Home, Icons.Outlined.Home)
    object Pantry : BottomNavItem("Pantry", Icons.Outlined.Inventory, Icons.Outlined.Inventory)
    object Recipe : BottomNavItem("Recipe", Icons.Filled.MenuBook, Icons.Outlined.MenuBook)
    object Reward : BottomNavItem("Reward", Icons.Filled.CardGiftcard, Icons.Outlined.CardGiftcard)
    object Settings : BottomNavItem("Settings", Icons.Outlined.Settings, Icons.Outlined.Settings)
    object Add : BottomNavItem("Add", Icons.Filled.Add, Icons.Filled.Add)
}

@Composable
fun RecipeContent(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Recipes You Can Make",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (recipes.isEmpty()) {
            Text(
                "You don't have the ingredients for any recipes yet. Add more items to your inventory!",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recipes) { recipe ->
                    RecipeCardWithImage(
                        recipe = recipe,
                        onClick = { onRecipeClick(recipe) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeCardWithImage(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            // Recipe image from Google Drive
            if (recipe.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = "${recipe.name} Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.white),
                    error = painterResource(R.drawable.white)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.RestaurantMenu,
                        contentDescription = "No Image",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // Recipe info
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = recipe.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ingredients: ${recipe.ingredients.joinToString(", ") { "${it.quantity} ${it.unit} ${it.name}" }}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipe: Recipe,
    onBackClick: () -> Unit,
    onMakeRecipe: (Recipe) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = recipe.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onMakeRecipe(recipe) },
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Filled.RestaurantMenu, "Make Recipe")
                    Text("Make Recipe")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .background(Color.White)
        ) {
            // Recipe image
            if (recipe.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(recipe.imageUrl)
                        .crossfade(true)
                        .scale(Scale.FILL)
                        .build(),
                    contentDescription = "${recipe.name} Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = rememberAsyncImagePainter(
                        model = R.drawable.white,
                    ),
                    error = rememberAsyncImagePainter(
                        model = R.drawable.white,
                    )
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.RestaurantMenu,
                        contentDescription = "No Image",
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            // Recipe content
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Ingredients",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                recipe.ingredients.forEach { ingredient ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "• ${ingredient.name}",
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${ingredient.quantity} ${ingredient.unit}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Instructions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val instructionSteps = recipe.instructions.split("\n").filter { it.isNotBlank() }
                instructionSteps.forEachIndexed { index, instruction ->
                    Text(
                        text = instruction,
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(bottom = 8.dp),
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PantryContent(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    fridgeItems: List<FoodItem>,
    freezerItems: List<FoodItem>,
    pantryItems: List<FoodItem>,
    onDeleteItem: (FoodItem) -> Unit,
    onFridgeItemsChange: (List<FoodItem>) -> Unit,
    onFreezerItemsChange: (List<FoodItem>) -> Unit,
    onPantryItemsChange: (List<FoodItem>) -> Unit
) {
    var selectedStorage by remember { mutableStateOf("Fridge") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top bar with menu button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "My Pantry",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color(0xFF4CAF50)
                )
            }
        }

        // Storage type selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Fridge", "Freezer", "Pantry").forEach { storage ->
                StorageTypeChip(
                    text = storage,
                    isSelected = selectedStorage == storage,
                    onClick = { selectedStorage = storage }
                )
            }
        }

        // Food items for selected storage
        when (selectedStorage) {
            "Fridge" -> FoodItemsSection(
                items = fridgeItems,
                onItemsChange = onFridgeItemsChange,
                emptyMessage = "No items in fridge",
                onDeleteItem = onDeleteItem
            )
            "Freezer" -> FoodItemsSection(
                items = freezerItems,
                onItemsChange = onFreezerItemsChange,
                emptyMessage = "No items in freezer",
                onDeleteItem = onDeleteItem
            )
            "Pantry" -> FoodItemsSection(
                items = pantryItems,
                onItemsChange = onPantryItemsChange,
                emptyMessage = "No items in pantry",
                onDeleteItem = onDeleteItem
            )
        }
    }
}

@Composable
fun StorageTypeChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(40.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF4CAF50) else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (isSelected) Color.White else Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun FoodItemsSection(
    items: List<FoodItem>,
    onItemsChange: (List<FoodItem>) -> Unit,
    emptyMessage: String,
    onDeleteItem: (FoodItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emptyMessage,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    FoodItemCard(
                        item = item,
                        onDelete = { onDeleteItem(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun FoodItemCard(
    item: FoodItem,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "${item.quantity} ${item.unit}",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = item.statusText,
                    fontSize = 14.sp,
                    color = when (item.status) {
                        FoodStatus.EXPIRED -> Color(0xFFF44336)
                        FoodStatus.EXPIRING_SOON -> Color(0xFFFF9800)
                        FoodStatus.FRESH -> Color(0xFF4CAF50)
                        else -> Color.Gray
                    }
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFF44336)
                )
            }
        }
    }
}

@Composable
fun SettingsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Text(
            text = "Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Settings content coming soon...",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}