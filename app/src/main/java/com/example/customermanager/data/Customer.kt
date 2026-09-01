package com.example.customermanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val address: String,
    val itemType: String,
    val totalAmount: Double,
    val paidAmount: Double,
    val remainingAmount: Double = totalAmount - paidAmount,
    val registrationDate: String,
    val lastInteractionDate: String,
    val notes: String
)
