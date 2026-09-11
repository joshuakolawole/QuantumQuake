package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM service_appointments ORDER BY timestamp DESC")
    fun getAllAppointments(): Flow<List<ServiceAppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: ServiceAppointmentEntity): Long

    @Delete
    suspend fun deleteAppointment(appointment: ServiceAppointmentEntity)

    @Query("DELETE FROM service_appointments WHERE id = :id")
    suspend fun deleteAppointmentById(id: Long)

    @Query("DELETE FROM service_appointments")
    suspend fun deleteAllAppointments()
}
