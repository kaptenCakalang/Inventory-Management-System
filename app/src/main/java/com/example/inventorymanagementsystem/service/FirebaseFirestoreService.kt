package com.example.inventorymanagementsystem.service

import com.example.inventorymanagementsystem.FoodItem // Import from main package
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val userId: String = "",
    val email: String = "",
    val fullName: String = "",
    val foodPreferences: List<String> = emptyList(),
    val storageLocations: List<String> = emptyList(),
    val reminderTime: String = "",
    val appGoals: List<String> = emptyList()
)

class FirebaseFirestoreService {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // User Profile Operations
    suspend fun saveUserProfile(userProfile: UserProfile): Result<Unit> {
        return try {
            db.collection("users")
                .document(userProfile.userId)
                .set(userProfile)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(userId: String): Result<UserProfile> {
        return try {
            val document = db.collection("users")
                .document(userId)
                .get()
                .await()

            if (document.exists()) {
                val userProfile = document.toObject(UserProfile::class.java)
                Result.success(userProfile!!)
            } else {
                Result.failure(Exception("User profile not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Food Items Operations
    suspend fun addFoodItem(foodItem: FoodItem): Result<String> {
        return try {
            val documentRef = db.collection("foodItems").add(foodItem).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFoodItems(userId: String): Result<List<FoodItem>> {
        return try {
            val querySnapshot = db.collection("foodItems")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val foodItems = querySnapshot.documents.map { document ->
                document.toObject(FoodItem::class.java)!!.copy(id = document.id)
            }
            Result.success(foodItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateFoodItem(itemId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            db.collection("foodItems")
                .document(itemId)
                .update(updates)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFoodItem(itemId: String): Result<Unit> {
        return try {
            db.collection("foodItems")
                .document(itemId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}