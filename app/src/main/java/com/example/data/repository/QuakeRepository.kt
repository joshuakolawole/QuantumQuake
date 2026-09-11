package com.example.data.repository

import com.example.data.local.AppointmentDao
import com.example.data.local.ConfigDao
import com.example.data.local.SavedConfigEntity
import com.example.data.local.ServiceAppointmentEntity
import kotlinx.coroutines.flow.Flow

class QuakeRepository(
    private val configDao: ConfigDao,
    private val appointmentDao: AppointmentDao
) {
    val allConfigs: Flow<List<SavedConfigEntity>> = configDao.getAllConfigs()
    val allAppointments: Flow<List<ServiceAppointmentEntity>> = appointmentDao.getAllAppointments()

    suspend fun saveConfig(config: SavedConfigEntity): Long {
        return configDao.insertConfig(config)
    }

    suspend fun deleteConfigById(id: Long) {
        configDao.deleteConfigById(id)
    }

    suspend fun clearAllConfigs() {
        configDao.deleteAllConfigs()
    }

    suspend fun bookAppointment(appointment: ServiceAppointmentEntity): Long {
        return appointmentDao.insertAppointment(appointment)
    }

    suspend fun cancelAppointmentById(id: Long) {
        appointmentDao.deleteAppointmentById(id)
    }

    suspend fun clearAllAppointments() {
        appointmentDao.deleteAllAppointments()
    }
}
