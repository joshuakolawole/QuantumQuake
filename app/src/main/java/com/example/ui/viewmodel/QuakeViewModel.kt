package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.SavedConfigEntity
import com.example.data.local.ServiceAppointmentEntity
import com.example.data.model.ActiveVehicleConfig
import com.example.data.model.BodyStyle
import com.example.data.model.DrivetrainOption
import com.example.data.model.ExteriorColor
import com.example.data.model.InteriorTrim
import com.example.data.model.MetricReading
import com.example.data.model.TelemetrySnapshot
import com.example.data.model.WheelOption
import com.example.data.model.generateQuantumId
import com.example.data.repository.QuakeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class QuakeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuakeRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = QuakeRepository(db.configDao(), db.appointmentDao())
    }

    // Vehicle Configuration State
    private val _activeConfig = MutableStateFlow(ActiveVehicleConfig())
    val activeConfig: StateFlow<ActiveVehicleConfig> = _activeConfig.asStateFlow()

    // Saved Garage Configurations
    val savedConfigs: StateFlow<List<SavedConfigEntity>> = repository.allConfigs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Service Appointments
    val serviceAppointments: StateFlow<List<ServiceAppointmentEntity>> = repository.allAppointments
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Live Telemetry
    private val _telemetry = MutableStateFlow(TelemetrySnapshot.initial())
    val telemetry: StateFlow<TelemetrySnapshot> = _telemetry.asStateFlow()

    // Account & Settings
    private val _serviceAlertsEnabled = MutableStateFlow(true)
    val serviceAlertsEnabled: StateFlow<Boolean> = _serviceAlertsEnabled.asStateFlow()

    private val _connectedVehicleVin = MutableStateFlow("QQ-2026-X847KW")
    val connectedVehicleVin: StateFlow<String> = _connectedVehicleVin.asStateFlow()

    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

    init {
        // Start telemetry simulation background loop
        startTelemetrySimulation()
    }

    private fun startTelemetrySimulation() {
        viewModelScope.launch {
            while (isActive) {
                delay(1200)
                if (_telemetry.value.isLiveStreaming) {
                    simulateTelemetryTick()
                }
            }
        }
    }

    private fun simulateTelemetryTick() {
        val current = _telemetry.value
        val updatedMetrics = current.metrics.map { m ->
            when (m.id) {
                "power" -> {
                    val p = 120 + Random.nextInt(75)
                    m.copy(value = "$p", intensity = (p / 100f).coerceIn(0.8f, 2.0f))
                }
                "motor_temp" -> {
                    val t = 62 + Random.nextInt(6)
                    m.copy(value = "$t")
                }
                "speed" -> {
                    val s = (112 + Random.nextInt(14)).coerceAtLeast(0)
                    m.copy(value = "$s")
                }
                "range" -> {
                    val r = 550 + Random.nextInt(15)
                    m.copy(value = "$r")
                }
                else -> m
            }
        }
        _telemetry.update {
            it.copy(
                metrics = updatedMetrics,
                lastUpdatedMillis = System.currentTimeMillis()
            )
        }
    }

    fun toggleLiveStream() {
        _telemetry.update { it.copy(isLiveStreaming = !it.isLiveStreaming) }
    }

    fun manualRefreshTelemetry() {
        viewModelScope.launch {
            simulateTelemetryTick()
            _userFeedbackMessage.value = "Sensors Synchronized"
            delay(1500)
            _userFeedbackMessage.value = null
        }
    }

    // Configurator actions
    fun setBodyStyle(style: BodyStyle) {
        _activeConfig.update { it.copy(bodyStyle = style) }
    }

    fun setExteriorColor(color: ExteriorColor) {
        _activeConfig.update { it.copy(exteriorColor = color) }
    }

    fun setInteriorTrim(trim: InteriorTrim) {
        _activeConfig.update { it.copy(interiorTrim = trim) }
    }

    fun setWheelOption(wheel: WheelOption) {
        _activeConfig.update { it.copy(wheelOption = wheel) }
    }

    fun setDrivetrain(drivetrain: DrivetrainOption) {
        _activeConfig.update { it.copy(drivetrain = drivetrain) }
    }

    fun setViewMode(mode: ActiveVehicleConfig.ViewMode) {
        _activeConfig.update { it.copy(viewMode = mode) }
    }

    fun generateNewQuantumId(): String {
        val newId = generateQuantumId()
        _activeConfig.update { it.copy(generatedQuantumId = newId) }
        return newId
    }

    fun resetQuantumId() {
        _activeConfig.update { it.copy(generatedQuantumId = null) }
    }

    fun saveCurrentConfigToGarage() {
        val config = _activeConfig.value
        val quantumId = config.generatedQuantumId ?: generateQuantumId()
        viewModelScope.launch {
            repository.saveConfig(
                SavedConfigEntity(
                    quantumId = quantumId,
                    bodyStyleId = config.bodyStyle.id,
                    modelName = config.bodyStyle.modelName,
                    exteriorColorId = config.exteriorColor.id,
                    exteriorColorName = config.exteriorColor.displayName,
                    interiorTrimId = config.interiorTrim.id,
                    wheelOptionId = config.wheelOption.id,
                    drivetrainId = config.drivetrain.id
                )
            )
            _activeConfig.update { it.copy(generatedQuantumId = quantumId) }
            _userFeedbackMessage.value = "Configuration Saved to Garage"
            delay(2000)
            _userFeedbackMessage.value = null
        }
    }

    fun loadSavedConfig(entity: SavedConfigEntity) {
        _activeConfig.update {
            ActiveVehicleConfig(
                bodyStyle = BodyStyle.fromId(entity.bodyStyleId),
                exteriorColor = ExteriorColor.fromId(entity.exteriorColorId),
                interiorTrim = InteriorTrim.fromId(entity.interiorTrimId),
                wheelOption = WheelOption.fromId(entity.wheelOptionId),
                drivetrain = DrivetrainOption.fromId(entity.drivetrainId),
                generatedQuantumId = entity.quantumId
            )
        }
        _userFeedbackMessage.value = "Loaded ${entity.modelName} (${entity.quantumId})"
        viewModelScope.launch {
            delay(2000)
            _userFeedbackMessage.value = null
        }
    }

    fun deleteSavedConfig(id: Long) {
        viewModelScope.launch {
            repository.deleteConfigById(id)
        }
    }

    fun clearAllSavedConfigs() {
        viewModelScope.launch {
            repository.clearAllConfigs()
            _userFeedbackMessage.value = "Garage Cleared"
            delay(1500)
            _userFeedbackMessage.value = null
        }
    }

    // Appointment actions
    fun bookAppointment(
        centerName: String,
        centerLocation: String,
        dateString: String,
        timeSlot: String,
        serviceType: String
    ) {
        viewModelScope.launch {
            repository.bookAppointment(
                ServiceAppointmentEntity(
                    centerName = centerName,
                    centerLocation = centerLocation,
                    dateString = dateString,
                    timeSlot = timeSlot,
                    serviceType = serviceType
                )
            )
            _userFeedbackMessage.value = "Appointment Confirmed: $dateString"
            delay(2500)
            _userFeedbackMessage.value = null
        }
    }

    fun cancelAppointment(id: Long) {
        viewModelScope.launch {
            repository.cancelAppointmentById(id)
            _userFeedbackMessage.value = "Appointment Cancelled"
            delay(1500)
            _userFeedbackMessage.value = null
        }
    }

    fun toggleServiceAlerts() {
        _serviceAlertsEnabled.update { !it }
    }

    fun updateVehicleVin(vin: String) {
        if (vin.isNotBlank()) {
            _connectedVehicleVin.value = vin.trim().uppercase()
        }
    }

    fun clearFeedbackMessage() {
        _userFeedbackMessage.value = null
    }
}
