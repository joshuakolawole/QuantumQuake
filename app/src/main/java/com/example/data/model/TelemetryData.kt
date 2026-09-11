package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.QuakeCobalt
import com.example.ui.theme.QuakeCyan
import com.example.ui.theme.QuakePlasmaRed

data class MetricReading(
    val id: String,
    val title: String,
    val value: String,
    val unit: String,
    val color: Color,
    val intensity: Float = 1.0f,
    val phaseOffset: Float = 0f
)

data class SystemStatusItem(
    val id: String,
    val label: String,
    val status: String,
    val isOk: Boolean,
    val iconName: String
)

data class TelemetrySnapshot(
    val metrics: List<MetricReading>,
    val systemStatuses: List<SystemStatusItem>,
    val isLiveStreaming: Boolean = true,
    val lastUpdatedMillis: Long = System.currentTimeMillis()
) {
    companion object {
        fun initial(): TelemetrySnapshot = TelemetrySnapshot(
            metrics = listOf(
                MetricReading("battery", "Battery Level", "82", "%", QuakeCobalt, 0.6f, 0f),
                MetricReading("range", "Range Remaining", "558", "km", QuakeCobalt, 0.7f, 1.2f),
                MetricReading("power", "Power Output", "148", "kW", QuakePlasmaRed, 1.5f, 2.4f),
                MetricReading("motor_temp", "Motor Temp", "64", "°C", QuakePlasmaRed, 0.9f, 3.6f),
                MetricReading("speed", "Speed", "118", "km/h", QuakeCobalt, 0.8f, 4.8f),
                MetricReading("trip", "Trip Duration", "1:42", "hrs", QuakeCyan, 0.4f, 5.5f)
            ),
            systemStatuses = listOf(
                SystemStatusItem("bat_health", "Battery Health", "Optimal", true, "battery"),
                SystemStatusItem("tire_pres", "Tire Pressure", "All Nominal (2.8 bar)", true, "tire"),
                SystemStatusItem("brake_temp", "Brake Temp", "Cool (88°C)", true, "brake"),
                SystemStatusItem("charging", "Charging", "Not Connected", false, "charging"),
                SystemStatusItem("gps", "GPS Signal", "Strong (12 Sats)", true, "gps"),
                SystemStatusItem("service", "Next Service", "In 4,200 km", true, "service")
            )
        )
    }
}
