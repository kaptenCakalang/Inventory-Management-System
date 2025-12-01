package com.example.inventorymanagementsystem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementsystem.service.FirebaseFirestoreService
import com.example.inventorymanagementsystem.FoodItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InventoryViewModel : ViewModel() {
    private val firestoreService = FirebaseFirestoreService()

    private val _foodItems = MutableStateFlow<List<FoodItem>>(emptyList())
    val foodItems: StateFlow<List<FoodItem>> = _foodItems

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    fun loadFoodItems(userId: String) {
        _loadingState.value = true
        viewModelScope.launch {
            val result = firestoreService.getFoodItems(userId)
            if (result.isSuccess) {
                _foodItems.value = result.getOrThrow()
            }
            _loadingState.value = false
        }
    }

    fun addFoodItem(foodItem: FoodItem, userId: String) {
        viewModelScope.launch {
            val itemWithUserId = foodItem.copy(userId = userId)
            firestoreService.addFoodItem(itemWithUserId)
            loadFoodItems(userId) // Reload items
        }
    }

    fun deleteFoodItem(itemId: String, userId: String) {
        viewModelScope.launch {
            firestoreService.deleteFoodItem(itemId)
            loadFoodItems(userId) // Reload items
        }
    }
}