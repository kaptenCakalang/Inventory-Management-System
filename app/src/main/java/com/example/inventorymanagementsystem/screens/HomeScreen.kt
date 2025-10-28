package com.example.inventorymanagementsystem.screens

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
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventorymanagementsystem.ui.theme.TemplateTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import com.example.inventorymanagementsystem.R

import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode


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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush

import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.items




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
                containerColor = Color(0xFF006A67), // Green
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
                containerColor = Color(0xFF006A67), // Blue
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
                containerColor = Color(0xFF006A67), // Orange
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

        // Main FAB (plus/close button) - FIXED to be circle
        FloatingActionButton(
            onClick = {
                expanded = !expanded // Toggle expand/collapse
            },
            containerColor = Color(0xFFFF9800),
            contentColor = Color.White,
            modifier = Modifier.size(56.dp),
            shape = CircleShape // ADD THIS LINE
        ) {
            // Animate between plus and close icons
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualEntryScreen(
    onBackClick: () -> Unit = {}
) {
    var itemName by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var selectedStorage by remember { mutableStateOf("") }
    var additionalNotes by remember { mutableStateOf("") }
    var showManualEntry by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Add Item",
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
                onClick = {
                    // Handle add item logic later
                },
                containerColor = Color(0xFFFF9800),
                contentColor = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .width(140.dp)
                    .height(56.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Item",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add Item",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
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
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Enter Item Name",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Custom TextField with character counter
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text(
                                text = "Enter item name",
                                color = Color.Gray
                            )
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

                    // Character counter
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

            Spacer(modifier = Modifier.height(24.dp))

            // Expiry Date Section
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Expiration Date",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    placeholder = {
                        Text(
                            text = "DD/MM/YYYY",
                            color = Color.Gray
                        )
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Storage Section
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Storage",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                var expanded by remember { mutableStateOf(false) }
                val storageOptions = listOf("Fridge", "Freezer", "Pantry")

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
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
                                Text(
                                    text = "Select",
                                    color = Color.Gray
                                )
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
                                        Text(
                                            text = option,
                                            fontSize = 16.sp
                                        )
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
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Additional Notes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = additionalNotes,
                    onValueChange = { additionalNotes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = {
                        Text(
                            text = "Add any additional notes here...",
                            color = Color.Gray
                        )
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

            // Add some extra space at the bottom for FAB
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    var selectedBottomNavItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }
    val scope = rememberCoroutineScope()

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
                            text = "K",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // User name
                    Text(
                        text = "Karina",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Divider()

                // Navigation items matching your reference
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        selectedBottomNavItem = BottomNavItem.Settings
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Search") },
                    selected = false,
                    onClick = {
                        // Add search functionality later
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Account") },
                    selected = false,
                    onClick = {
                        // Add account functionality later
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Account"
                        )
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Badge & Achievements") },
                    selected = false,
                    onClick = {
                        // Add badges functionality later
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.EmojiEvents,
                            contentDescription = "Badges"
                        )
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Notification") },
                    selected = false,
                    onClick = {
                        // Add notification functionality later
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Language") },
                    selected = false,
                    onClick = {
                        // Add language functionality later
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Language,
                            contentDescription = "Language"
                        )
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Privacy Policy") },
                    selected = false,
                    onClick = {
                        // Add privacy policy functionality later
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Policy,
                            contentDescription = "Privacy Policy"
                        )
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Help Center") },
                    selected = false,
                    onClick = {
                        // Add help center functionality later
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Help,
                            contentDescription = "Help Center"
                        )
                    }
                )

                NavigationDrawerItem(
                    label = { Text("About") },
                    selected = false,
                    onClick = {
                        // Add about functionality later
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "About"
                        )
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                if (selectedRecipe == null) { // Only show bottom bar when NOT in recipe detail
                    // Custom Bottom Bar with FAB
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp) // Increased height to accommodate the FAB
                    ) {
                        // Background image
                        Image(
                            painter = painterResource(id = R.drawable.bottom_nav_background),
                            contentDescription = "Bottom Navigation Background",
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.BottomCenter),
                            contentScale = ContentScale.FillBounds
                        )

                        // Rest of your bottom bar content (NavigationBar and FAB)
                        NavigationBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(Color.Transparent),
                            containerColor = Color.Transparent// Make navigation bar transparent
                        ) {
                            val navItems = listOf(
                                BottomNavItem.Home,
                                BottomNavItem.Pantry,
                                BottomNavItem.Recipe,
                                BottomNavItem.Reward
                            )

                            // Left side items (Home, Pantry)
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
                                                Color(0xFF333333) // Dark color for selected icons
                                            } else {
                                                Color.White // White for unselected icons
                                            }
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            color = if (selectedBottomNavItem == item) {
                                                Color(0xFF333333) // Dark color for selected labels
                                            } else {
                                                Color.White // White for unselected labels
                                            }
                                        )
                                    }
                                )
                            }

                            // Empty item as spacer for the FAB
                            NavigationBarItem(
                                selected = false,
                                onClick = {},
                                icon = {},
                                label = {}
                            )

                            // Right side items (Recipe, Reward)
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
                                                Color(0xFF333333) // Dark color for selected icons
                                            } else {
                                                Color.White // White for unselected icons
                                            }
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            color = if (selectedBottomNavItem == item) {
                                                Color(0xFF333333) // Dark color for selected labels
                                            } else {
                                                Color.White // White for unselected labels
                                            }
                                        )
                                    }
                                )
                            }
                        }

                        // Floating Action Button centered above the navigation bar
                        // Expandable FAB centered above the navigation bar
                        ExpandableFAB(
                            onMainClick = {
                                // Do nothing - the expansion is handled internally
                            },
                            onKeyboardClick = {
                                // Show manual entry screen or dialog
                                selectedBottomNavItem = BottomNavItem.Add
                            },
                            onScanClick = {
                                // Show scan screen or dialog
                                // We'll add this functionality later
                            },
                            onMicrophoneClick = {
                                // Show voice input screen or dialog
                                // We'll add this functionality later
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
            // Show recipe detail as overlay, or show the regular content
            if (selectedRecipe != null) {
                RecipeDetailScreen(
                    recipe = selectedRecipe!!,
                    onBackClick = { selectedRecipe = null }
                )
            } else {
                when (selectedBottomNavItem) {
                    is BottomNavItem.Home -> HomeDashboardContent(
                        modifier = Modifier.padding(innerPadding),
                        onMenuClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                    is BottomNavItem.Pantry -> PantryContent(
                        modifier = Modifier.padding(innerPadding),
                        onMenuClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                    is BottomNavItem.Recipe -> RecipeContent(
                        onRecipeClick = { recipe ->
                            selectedRecipe = recipe
                        }
                    )
                    is BottomNavItem.Reward -> ComingSoonContent("Reward")
                    is BottomNavItem.Settings -> SettingsContent()
                    is BottomNavItem.Add -> ManualEntryScreen(
                        onBackClick = {
                            selectedBottomNavItem = BottomNavItem.Home // Go back to home
                        }
                    )
                }
            }
        }
    }
}




// Add this new composable for coming soon pages
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
    onMenuClick: () -> Unit = {}
) {
    // State to track which tab is selected
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Expired", "Expiring Soon", "Fresh")

    // STATEFUL food items for each tab - this is the key fix!
    val expiredItems = remember { mutableStateListOf(
        FoodItem("French Fries", "Expired 2 days ago", FoodStatus.EXPIRED),
        FoodItem("Yogurt", "Expired yesterday", FoodStatus.EXPIRED),
        FoodItem("Honey", "Expired 5 days ago", FoodStatus.EXPIRED),
        FoodItem("Bread", "Expired 3 days ago", FoodStatus.EXPIRED),
        FoodItem("Eggs", "Expired 1 day ago", FoodStatus.EXPIRED),
        FoodItem("Cheese", "Expired 4 days ago", FoodStatus.EXPIRED),
        FoodItem("Tomatoes", "Expired 2 days ago", FoodStatus.EXPIRED)
    ) }

    val expiringSoonItems = remember { mutableStateListOf(
        FoodItem("Milk", "Expires in 2 days", FoodStatus.EXPIRING_SOON),
        FoodItem("Chicken", "Expires in 3 days", FoodStatus.EXPIRING_SOON),
        FoodItem("Salad", "Expires tomorrow", FoodStatus.EXPIRING_SOON),
        FoodItem("Yogurt", "Expires in 4 days", FoodStatus.EXPIRING_SOON)
    ) }

    val freshItems = remember { mutableStateListOf(
        FoodItem("Apples", "Fresh for 14 days", FoodStatus.FRESH),
        FoodItem("Carrots", "Fresh for 21 days", FoodStatus.FRESH),
        FoodItem("Potatoes", "Fresh for 30 days", FoodStatus.FRESH),
        FoodItem("Onions", "Fresh for 45 days", FoodStatus.FRESH),
        FoodItem("Rice", "Fresh for 180 days", FoodStatus.FRESH),
        FoodItem("Pasta", "Fresh for 365 days", FoodStatus.FRESH)
    ) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White) // CHANGED: White background for the main content
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
                        text = "K",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Text Column - This will be centered
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Hi, Karina",
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

            // Clickable navigation tabs - UPDATED to show actual counts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                tabs.forEachIndexed { index, tab ->
                    StockStatusItem(
                        count = when (index) {
                            0 -> expiredItems.size    // Actual count from stateful list
                            1 -> expiringSoonItems.size   // Actual count from stateful list
                            else -> freshItems.size // Actual count from stateful list
                        },
                        label = tab,
                        color = when (index) {
                            0 -> Color(0xFFF44336)    // Red for Expired
                            1 -> Color(0xFFFF9800)    // Orange for Expiring Soon
                            else -> Color(0xFF4CAF50) // Green for Fresh
                        },
                        isSelected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }

        // Food list section - UPDATED to use stateful lists
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F9FF)), // CHANGED: Blue-ish white for card
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            // Different content based on selected tab - NOW USING STATEFUL LISTS
            when (selectedTab) {
                0 -> FoodItemsColumnWithDelete( // Expired tab
                    items = expiredItems,
                    onDeleteItem = { item ->
                        // ACTUALLY REMOVE the item from the list
                        expiredItems.remove(item)
                    }
                )
                1 -> FoodItemsColumnWithDelete( // Expiring Soon tab
                    items = expiringSoonItems,
                    onDeleteItem = { item ->
                        // ACTUALLY REMOVE the item from the list
                        expiringSoonItems.remove(item)
                    }
                )
                2 -> FoodItemsColumnWithDelete( // Fresh tab
                    items = freshItems,
                    onDeleteItem = { item ->
                        // ACTUALLY REMOVE the item from the list
                        freshItems.remove(item)
                    }
                )
            }
        }
    }
}


@Composable
fun PantryContent(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {}
) {
    // State to track which storage tab is selected
    var selectedStorageTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    val storageTabs = listOf("Fridge", "Freezer", "Pantry")

    // Food stock counts (non-clickable, just for display)
    val expiredCount = 3
    val expiringSoonCount = 9
    val freshCount = 18

    // Stateful food items for each storage section
    val fridgeItems = remember { mutableStateListOf(
        FoodItem("Yogurt", "Expired", FoodStatus.EXPIRED),
        FoodItem("Milk", "Expires in 12 days", FoodStatus.EXPIRING_SOON),
        FoodItem("Chocolate Sauce", "Expires in 18 days", FoodStatus.FRESH),
        FoodItem("Eggs", "Expires in 28 days", FoodStatus.FRESH)
    ) }

    val freezerItems = remember { mutableStateListOf(
        FoodItem("Frozen Berries", "Expires in 60 days", FoodStatus.FRESH),
        FoodItem("Ice Cream", "Expires in 30 days", FoodStatus.FRESH)
    ) }

    val pantryItems = remember { mutableStateListOf(
        FoodItem("Pasta", "Expires in 365 days", FoodStatus.FRESH),
        FoodItem("Rice", "Expires in 180 days", FoodStatus.FRESH),
        FoodItem("Canned Beans", "Expires in 90 days", FoodStatus.FRESH)
    ) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White) // CHANGED: White background for the main content
            .padding(16.dp)
    ) {
        // Fixed top section - SAME AS HOMEPAGE
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
                        text = "K",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Text Column - This will be centered
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Hi, Karina",
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

            // Date section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tuesday, 9 September",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Check Your Food Stock!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            // FOOD STOCK STATUS - NON-CLICKABLE (same as homepage but not interactive)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Expired
                StockStatusItem(
                    count = expiredCount,
                    label = "Expired",
                    color = Color(0xFFF44336),
                    isSelected = false,
                    onClick = {} // No click action
                )

                // Expiring Soon
                StockStatusItem(
                    count = expiringSoonCount,
                    label = "Expiring Soon",
                    color = Color(0xFFFF9800),
                    isSelected = false,
                    onClick = {} // No click action
                )

                // Fresh
                StockStatusItem(
                    count = freshCount,
                    label = "Fresh",
                    color = Color(0xFF4CAF50),
                    isSelected = false,
                    onClick = {} // No click action
                )
            }

            // SEARCH BAR
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = {
                    Text(
                        text = "Got any specific ingredients in mind?",
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp)
            )

            // STORAGE TYPE TABS - CLICKABLE (Fridge/Freezer/Pantry)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,

            ) {
                storageTabs.forEachIndexed { index, tab ->
                    StockStatusItem(
                        count = when (index) {
                            0 -> fridgeItems.size
                            1 -> freezerItems.size
                            else -> pantryItems.size
                        },
                        label = tab,
                        color = when (index) {
                            0 -> Color(0xFF2196F3)    // Blue for Fridge
                            1 -> Color(0xFF03A9F4)    // Light Blue for Freezer
                            else -> Color(0xFF4CAF50) // Green for Pantry
                        },
                        isSelected = selectedStorageTab == index,
                        onClick = { selectedStorageTab = index }
                    )
                }
            }
        }

        // Food list section - PURE LIST WITHOUT HEADERS
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F9FF)), // CHANGED: Blue-ish white for card
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            // Different content based on selected storage tab - NO HEADER TEXT
            when (selectedStorageTab) {
                0 -> PureFoodItemsColumn( // Fridge tab - just the list
                    items = fridgeItems,
                    onDeleteItem = { item ->
                        fridgeItems.remove(item)
                    }
                )
                1 -> PureFoodItemsColumn( // Freezer tab - just the list
                    items = freezerItems,
                    onDeleteItem = { item ->
                        freezerItems.remove(item)
                    }
                )
                2 -> PureFoodItemsColumn( // Pantry tab - just the list
                    items = pantryItems,
                    onDeleteItem = { item ->
                        pantryItems.remove(item)
                    }
                )
            }
        }
    }
}

// NEW: Pure food items column without any header text
@Composable
fun PureFoodItemsColumn(
    items: List<FoodItem>,
    onDeleteItem: (FoodItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // NO HEADER TEXT - just the pure list of items

        // Use regular Column for the list
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                FoodItemWithDelete(
                    foodItem = item,
                    onDelete = { onDeleteItem(item) }
                )
            }
        }
    }
}

// Pantry item data class
data class PantryItem(
    val name: String,
    val category: String,
    val quantity: String,
    val expiryDate: String,
    val addedDate: String,
    val imageRes: Int
) {
    fun daysUntilExpiry(): Int {
        // Simple implementation - in real app, parse the date and calculate
        return when (expiryDate) {
            "2024-09-11" -> 1
            "2024-09-12" -> 2
            "2024-09-13" -> 3
            "2024-09-20" -> 10
            "2024-09-25" -> 15
            "2024-09-30" -> 20
            "2024-10-05" -> 25
            "2024-12-01" -> 85
            else -> 0
        }
    }

    fun getExpiryStatus(): ExpiryStatus {
        val days = daysUntilExpiry()
        return when {
            days <= 0 -> ExpiryStatus.EXPIRED
            days <= 3 -> ExpiryStatus.EXPIRING_SOON
            else -> ExpiryStatus.FRESH
        }
    }
}

enum class ExpiryStatus {
    EXPIRED, EXPIRING_SOON, FRESH
}

// Stat card composable
@Composable
fun StatCard(
    count: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Pantry item card composable
@Composable
fun PantryItemCard(
    item: PantryItem,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Item image
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Item details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "${item.quantity} • ${item.category}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )

                // Expiry info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    val status = item.getExpiryStatus()
                    val statusColor = when (status) {
                        ExpiryStatus.EXPIRED -> Color(0xFFF44336)
                        ExpiryStatus.EXPIRING_SOON -> Color(0xFFFF9800)
                        ExpiryStatus.FRESH -> Color(0xFF4CAF50)
                    }
                    val statusText = when (status) {
                        ExpiryStatus.EXPIRED -> "Expired"
                        ExpiryStatus.EXPIRING_SOON -> "Expires in ${item.daysUntilExpiry()} days"
                        ExpiryStatus.FRESH -> "Fresh - ${item.daysUntilExpiry()} days left"
                    }

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )

                    Text(
                        text = statusText,
                        fontSize = 12.sp,
                        color = statusColor,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            // Action buttons
            Row {
                // Edit button
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit ${item.name}",
                        tint = Color(0xFF2196F3)
                    )
                }

                // Delete button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete ${item.name}",
                        tint = Color(0xFFF44336)
                    )
                }
            }
        }
    }
}




// Food status enum
enum class FoodStatus {
    EXPIRED, EXPIRING_SOON, FRESH
}

// Food item data class
data class FoodItem(
    val name: String,
    val statusText: String,
    val status: FoodStatus
)

// NEW: Food item with delete button - matches your reference design
// UPDATED: Food item with delete button - without status badge
@Composable
fun FoodItemWithDelete(
    foodItem: FoodItem,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Food info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = foodItem.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = foodItem.statusText,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // DELETE BUTTON ONLY - removed the status badge
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete ${foodItem.name}",
                    tint = Color(0xFFF44336) // Red color for delete
                )
            }
        }
    }
}

// NEW: Food items column with delete functionality (using regular Column)
@Composable
fun FoodItemsColumnWithDelete(
    items: List<FoodItem>,
    onDeleteItem: (FoodItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = when (items.firstOrNull()?.status) {
                FoodStatus.EXPIRED -> "Expired Items"
                FoodStatus.EXPIRING_SOON -> "Expiring Soon"
                FoodStatus.FRESH -> "Fresh Items"
                null -> "Items"
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        )

        // Use regular Column instead of LazyColumn
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                FoodItemWithDelete(
                    foodItem = item,
                    onDelete = { onDeleteItem(item) }
                )
            }
        }
    }
}




// UPDATED: StockStatusItem to match reference exactly
@Composable
fun StockStatusItem(
    count: Int,
    label: String,
    color: Color,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = count.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
}




@Composable
fun ExpiredFoodItem(name: String, daysAgo: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = daysAgo,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Badge(
                containerColor = Color(0xFFF44336)
            ) {
                Text(
                    text = "Expired",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ExpiringSoonItem(name: String, daysLeft: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = daysLeft,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Badge(
                containerColor = Color(0xFFFF9800)
            ) {
                Text(
                    text = "Soon",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}
// New composable for Fresh items
@Composable
fun FreshFoodItem(name: String, status: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = status,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Badge(
                containerColor = Color(0xFF4CAF50)
            ) {
                Text(
                    text = "Fresh",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun InventoryContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Inventory,
            contentDescription = "Inventory",
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF4CAF50)
        )
        Text(
            text = "Inventory Management",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            text = "Manage your food items here",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
fun RemindersContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = "Reminders",
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF4CAF50)
        )
        Text(
            text = "Expiry Reminders",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            text = "View and manage your expiry reminders",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
fun SettingsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = "Settings",
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF4CAF50)
        )
        Text(
            text = "Settings",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            text = "Configure your app settings",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
fun RecipeContent(
    onRecipeClick: (Recipe) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var showSearchScreen by remember { mutableStateOf(false) }

    if (showSearchScreen) {
        RecipeSearchScreen(
            onBackClick = { showSearchScreen = false },
            onRecipeClick = onRecipeClick
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Header section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Recipe",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "From your",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Text(
                    text = "20 Ingredients",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )

                Text(
                    text = "discover 70 ways to cook.",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search bar - NOW CLICKABLE TO OPEN SEARCH SCREEN
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    if (it.isNotEmpty()) {
                        showSearchScreen = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { showSearchScreen = true },
                placeholder = {
                    Text(
                        text = "What do you want to cook today?",
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = false // Make it non-editable, only clickable
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Recipe grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(updatedRecipeItems) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { onRecipeClick(recipe) }
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeSearchScreen(
    onBackClick: () -> Unit,
    onRecipeClick: (Recipe) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    // State to track which recipes to show based on selection
    var filteredRecipes by remember { mutableStateOf(updatedRecipeItems) }
    var currentFilter by remember { mutableStateOf("All Recipes") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Recipe Search",
                        fontWeight = FontWeight.Bold
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Search bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = {
                    Text(
                        text = "What do you want to cook today?",
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Filter sections
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Recent & Quick Filters Section
                item {
                    Column {
                        Text(
                            text = "Recent & Quick Filters",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        FlowRow(
                            mainAxisSpacing = 12.dp,
                            crossAxisSpacing = 8.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterChip(
                                selected = currentFilter == "Recent",
                                onClick = {
                                    currentFilter = "Recent"
                                    filteredRecipes = updatedRecipeItems.take(2)
                                },
                                label = {
                                    Text(
                                        "Recent",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Under 15 Minutes",
                                onClick = {
                                    currentFilter = "Under 15 Minutes"
                                    filteredRecipes = updatedRecipeItems.filter { it.cookingTime.contains("10 min") || it.cookingTime.contains("15 min") }
                                },
                                label = {
                                    Text(
                                        "Under 15 Minutes",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Beginner-Friendly",
                                onClick = {
                                    currentFilter = "Beginner-Friendly"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Vegetable") || it.name.contains("Pasta") }
                                },
                                label = {
                                    Text(
                                        "Beginner-Friendly",
                                        softWrap = false
                                    )
                                }
                            )
                        }
                    }
                }

                // Difficulty Section
                item {
                    Column {
                        Text(
                            text = "Difficulty",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        FlowRow(
                            mainAxisSpacing = 12.dp,
                            crossAxisSpacing = 8.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterChip(
                                selected = currentFilter == "Beginner-Friendly",
                                onClick = {
                                    currentFilter = "Beginner-Friendly"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Vegetable") || it.name.contains("Pasta") }
                                },
                                label = {
                                    Text(
                                        "Beginner-Friendly",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Intermediate",
                                onClick = {
                                    currentFilter = "Intermediate"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Chicken") || it.cookingTime.contains("20 min") }
                                },
                                label = {
                                    Text(
                                        "Intermediate",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Advance",
                                onClick = {
                                    currentFilter = "Advance"
                                    filteredRecipes = updatedRecipeItems.filter { it.cookingTime.contains("30 min") || it.name.contains("Curry") }
                                },
                                label = {
                                    Text(
                                        "Advance",
                                        softWrap = false
                                    )
                                }
                            )
                        }
                    }
                }

                // Cooking Time Section
                item {
                    Column {
                        Text(
                            text = "Cooking Time",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        FlowRow(
                            mainAxisSpacing = 12.dp,
                            crossAxisSpacing = 8.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterChip(
                                selected = currentFilter == "Under 15 Minutes",
                                onClick = {
                                    currentFilter = "Under 15 Minutes"
                                    filteredRecipes = updatedRecipeItems.filter { it.cookingTime.contains("10 min") }
                                },
                                label = {
                                    Text(
                                        "Under 15 Minutes",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Under 30 Minutes",
                                onClick = {
                                    currentFilter = "Under 30 Minutes"
                                    filteredRecipes = updatedRecipeItems.filter { it.cookingTime.contains("20 min") }
                                },
                                label = {
                                    Text(
                                        "Under 30 Minutes",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Under 45 Minutes",
                                onClick = {
                                    currentFilter = "Under 45 Minutes"
                                    filteredRecipes = updatedRecipeItems.filter { it.cookingTime.contains("30 min") }
                                },
                                label = {
                                    Text(
                                        "Under 45 Minutes",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "More Than 1 Hour",
                                onClick = {
                                    currentFilter = "More Than 1 Hour"
                                    filteredRecipes = updatedRecipeItems.filter { it.cookingTime.contains("45 min") || it.cookingTime.contains("1 hour") }
                                },
                                label = {
                                    Text(
                                        "More Than 1 Hour",
                                        softWrap = false
                                    )
                                }
                            )
                        }
                    }
                }

                // Continue with the same pattern for Dish Type and Diet sections...
                // Dish Type Section
                item {
                    Column {
                        Text(
                            text = "Dish Type",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        FlowRow(
                            mainAxisSpacing = 12.dp,
                            crossAxisSpacing = 8.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterChip(
                                selected = currentFilter == "Breakfast",
                                onClick = {
                                    currentFilter = "Breakfast"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Pancakes") || it.name.contains("Omelette") }
                                },
                                label = {
                                    Text(
                                        "Breakfast",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Lunch",
                                onClick = {
                                    currentFilter = "Lunch"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Salad") || it.name.contains("Wrap") }
                                },
                                label = {
                                    Text(
                                        "Lunch",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Brunch",
                                onClick = {
                                    currentFilter = "Brunch"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("French Toast") || it.name.contains("Quiche") }
                                },
                                label = {
                                    Text(
                                        "Brunch",
                                        softWrap = false // FIX: Prevent text wrapping
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Dinner",
                                onClick = {
                                    currentFilter = "Dinner"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Pasta") || it.name.contains("Curry") || it.name.contains("Chicken") }
                                },
                                label = {
                                    Text(
                                        "Dinner",
                                        softWrap = false // FIX: Prevent text wrapping
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Appetizers",
                                onClick = {
                                    currentFilter = "Appetizers"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Dip") || it.name.contains("Bruschetta") }
                                },
                                label = {
                                    Text(
                                        "Appetizers",
                                        softWrap = false // FIX: Prevent text wrapping
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Snacks",
                                onClick = {
                                    currentFilter = "Snacks"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Trail Mix") || it.name.contains("Granola") }
                                },
                                label = {
                                    Text(
                                        "Snacks",
                                        softWrap = false // FIX: Prevent text wrapping
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Dessert",
                                onClick = {
                                    currentFilter = "Dessert"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Cake") || it.name.contains("Pudding") }
                                },
                                label = {
                                    Text(
                                        "Dessert",
                                        softWrap = false // FIX: Prevent text wrapping
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Drinks",
                                onClick = {
                                    currentFilter = "Drinks"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Smoothie") || it.name.contains("Juice") }
                                },
                                label = {
                                    Text(
                                        "Drinks",
                                        softWrap = false // FIX: Prevent text wrapping
                                    )
                                }
                            )

                            // ... continue with other Dish Type options
                        }


                    }
                }
                // Diet Section
                item {
                    Column {
                        Text(
                            text = "Diet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        FlowRow(
                            mainAxisSpacing = 12.dp,
                            crossAxisSpacing = 8.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterChip(
                                selected = currentFilter == "Gluten-Free",
                                onClick = {
                                    currentFilter = "Gluten-Free"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Rice") || it.name.contains("Vegetable") }
                                },
                                label = {
                                    Text(
                                        "Gluten-Free",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Dairy-Free",
                                onClick = {
                                    currentFilter = "Dairy-Free"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Vegan") || it.name.contains("Stir Fry") }
                                },
                                label = {
                                    Text(
                                        "Dairy-Free",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Low-Calorie",
                                onClick = {
                                    currentFilter = "Low-Calorie"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Salad") || it.name.contains("Vegetable") }
                                },
                                label = {
                                    Text(
                                        "Low-Calorie",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Vegan",
                                onClick = {
                                    currentFilter = "Vegan"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Vegetable") || it.name.contains("Stir Fry") }
                                },
                                label = {
                                    Text(
                                        "Vegan",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Low-Carb",
                                onClick = {
                                    currentFilter = "Low-Carb"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Chicken") || it.name.contains("Salad") }
                                },
                                label = {
                                    Text(
                                        "Low-Carb",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Vegetarian",
                                onClick = {
                                    currentFilter = "Vegetarian"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Vegetable") || it.name.contains("Pasta") }
                                },
                                label = {
                                    Text(
                                        "Vegetarian",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "High-Protein",
                                onClick = {
                                    currentFilter = "High-Protein"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Chicken") || it.name.contains("Sausage") }
                                },
                                label = {
                                    Text(
                                        "High-Protein",
                                        softWrap = false
                                    )
                                }
                            )

                            FilterChip(
                                selected = currentFilter == "Halal",
                                onClick = {
                                    currentFilter = "Halal"
                                    filteredRecipes = updatedRecipeItems.filter { it.name.contains("Chicken") || it.name.contains("Vegetable") }
                                },
                                label = {
                                    Text(
                                        "Halal",
                                        softWrap = false
                                    )
                                }
                            )
                        }
                    }
                }



                // Recipe Results Section
                item {
                    Column {
                        Text(
                            text = "Results (${filteredRecipes.size})",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Recipe grid
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.height(400.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(filteredRecipes) { recipe ->
                                RecipeCard(
                                    recipe = recipe,
                                    onClick = { onRecipeClick(recipe) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Recipe data class
data class Recipe(
    val name: String,
    val cookingTime: String,
    val imageRes: Int,
    val description: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val serves: String = "1/8"
)

data class Ingredient(
    val name: String,
    val quantity: String
)
// Update your recipe data with detailed information
val updatedRecipeItems = listOf(
    Recipe(
        name = "Cajun Sausage Pasta",
        cookingTime = "20 min",
        imageRes = R.drawable.cajun_sausage_pasta,
        description = "This pasta dish is packed with flavor and is perfect for a quick and easy weeknight meal. Fennel and sausage pair perfectly with the pasta for a delicious and satisfying dinner.",
        ingredients = listOf(
            Ingredient("Tomato Paste", "60/70 g"),
            Ingredient("Sausage", "40 g"),
            Ingredient("Pasta", "80 g"),
            Ingredient("Mozzarella", "15 g"),
            Ingredient("Parmesan Cheese", "100 g")
        ),
        serves = "1/8"
    ),
    Recipe(
        name = "Vegetable Stir Fry",
        cookingTime = "10 min",
        imageRes = R.drawable.vegetable_stir_fry,
        description = "A quick and healthy vegetable stir fry that's perfect for a light lunch or dinner.",
        ingredients = listOf(
            Ingredient("Mixed Vegetables", "200 g"),
            Ingredient("Soy Sauce", "2 tbsp"),
            Ingredient("Garlic", "2 cloves"),
            Ingredient("Ginger", "1 tsp")
        ),
        serves = "1/4"
    ),
    Recipe(
        name = "Chicken Curry",
        cookingTime = "30 min",
        imageRes = R.drawable.chicken_curry,
        description = "A rich and flavorful chicken curry with aromatic spices and creamy sauce.",
        ingredients = listOf(
            Ingredient("Chicken Breast", "300 g"),
            Ingredient("Coconut Milk", "400 ml"),
            Ingredient("Curry Powder", "2 tbsp"),
            Ingredient("Onion", "1 medium")
        ),
        serves = "1/6"
    )
)

// UPDATED: RecipeCard to be clickable
@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = recipe.imageRes),
                contentDescription = recipe.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            ),
                            startY = 300f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = recipe.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = recipe.cookingTime,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipe: Recipe,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Recipe",
                        fontWeight = FontWeight.Bold
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .background(Color.White)
        ) {
            // Recipe Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Image(
                    painter = painterResource(id = recipe.imageRes),
                    contentDescription = recipe.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Prep time and Cook time section - ADDED like your reference
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Prep Time
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Prep time",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "5 mins", // You can add prep time to your Recipe data class if needed
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // Cook Time
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Cook time",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = recipe.cookingTime,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            // Recipe Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Recipe Title
                Text(
                    text = recipe.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Recipe Description
                Text(
                    text = recipe.description,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Ingredients Section
                Text(
                    text = "Ingredients",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Serves section
                Text(
                    text = "Serves ${recipe.serves}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Ingredients List
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    recipe.ingredients.forEach { ingredient ->
                        IngredientRow(ingredient.name, ingredient.quantity)
                    }
                }
            }
        }
    }
}
// Helper composable for ingredient rows
@Composable
fun IngredientRow(ingredient: String, quantity: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = ingredient,
            fontSize = 16.sp,
            color = Color.Black
        )
        Text(
            text = quantity,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}








// BottomNavItem sealed class (keep this at the bottom of the file)
sealed class BottomNavItem(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
) {
    object Home : BottomNavItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = "home"
    )

    object Pantry : BottomNavItem(
        title = "Pantry",
        selectedIcon = Icons.Filled.Inventory,
        unselectedIcon = Icons.Outlined.Inventory,
        route = "pantry"
    )

    object Recipe : BottomNavItem(
        title = "Recipe",
        selectedIcon = Icons.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.MenuBook,
        route = "recipe"
    )

    object Reward : BottomNavItem(
        title = "Reward",
        selectedIcon = Icons.Filled.CardGiftcard,
        unselectedIcon = Icons.Outlined.CardGiftcard,
        route = "reward"
    )

    object Settings : BottomNavItem(
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = "settings"
    )
    // New Add item for the FAB
    object Add : BottomNavItem(
        title = "Add",
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Filled.Add,
        route = "add"
    )
}

//@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TemplateTheme {
        HomeScreen()
    }
}