package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_configurations")
data class SavedConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val quantumId: String,
    val bodyStyleId: String,
    val modelName: String,
    val exteriorColorId: String,
    val exteriorColorName: String,
    val interiorTrimId: String,
    val wheelOptionId: String,
    val drivetrainId: String,
    val timestamp: Long = System.currentTimeMillis()
)
