package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_appointments")
data class ServiceAppointmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val centerName: String,
    val centerLocation: String,
    val dateString: String,
    val timeSlot: String,
    val serviceType: String,
    val vehicleRef: String = "QQ-2026",
    val status: String = "CONFIRMED",
    val timestamp: Long = System.currentTimeMillis()
)
