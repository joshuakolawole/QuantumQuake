package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.StatusIndicator
import com.example.ui.components.TelemetryCard
import com.example.ui.theme.CarbonBackground
import com.example.ui.theme.CarbonBorder
import com.example.ui.theme.CarbonBorderSubtle
import com.example.ui.theme.CarbonSurface
import com.example.ui.theme.CarbonSurfaceVariant
import com.example.ui.theme.QuakeAmber
import com.example.ui.theme.QuakeCobalt
import com.example.ui.theme.QuakeCyan
import com.example.ui.theme.QuakeEmerald
import com.example.ui.theme.QuakePlasmaRed
import com.example.ui.theme.QuakeTextFaint
import com.example.ui.theme.QuakeTextMuted
import com.example.ui.theme.QuakeTextWhite
import com.example.ui.viewmodel.QuakeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PulseGridScreen(
    viewModel: QuakeViewModel,
    modifier: Modifier = Modifier
) {
    val telemetry by viewModel.telemetry.collectAsState()
    val appointments by viewModel.serviceAppointments.collectAsState()
    val serviceAlertsEnabled by viewModel.serviceAlertsEnabled.collectAsState()
    val connectedVin by viewModel.connectedVehicleVin.collectAsState()

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var showBookingSheet by remember { mutableStateOf(false) }
    var showVinDialog by remember { mutableStateOf(false) }
    var vinInput by remember { mutableStateOf(connectedVin) }
    var seismicHapticsEnabled by remember { mutableStateOf(true) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonBackground)
            .verticalScroll(scrollState)
            .testTag("pulse_grid_screen")
    ) {
        // Telemetry Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CarbonSurface)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "LIVE TELEMETRY",
                        style = MaterialTheme.typography.labelSmall,
                        color = QuakeCobalt,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "PULSE GRID",
                        style = MaterialTheme.typography.headlineMedium,
                        color = QuakeTextWhite,
                        letterSpacing = 1.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Live Stream Toggle Button
                    OutlinedButton(
                        onClick = { viewModel.toggleLiveStream() },
                        shape = RoundedCornerShape(2.dp),
                        border = BorderStroke(1.dp, CarbonBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (telemetry.isLiveStreaming) QuakeEmerald.copy(alpha = 0.1f) else CarbonSurface,
                            contentColor = QuakeTextWhite
                        ),
                        modifier = Modifier.testTag("toggle_stream_button")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (telemetry.isLiveStreaming) QuakeEmerald else QuakeAmber)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (telemetry.isLiveStreaming) "LIVE" else "PAUSED",
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 1.sp
                        )
                    }

                    // Refresh Button
                    IconButton(
                        onClick = { viewModel.manualRefreshTelemetry() },
                        modifier = Modifier
                            .size(36.dp)
                            .border(1.dp, CarbonBorder, RoundedCornerShape(2.dp))
                            .testTag("refresh_sensors_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh sensors",
                            tint = QuakeTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Real-time vehicle diagnostics and performance metrics",
                style = MaterialTheme.typography.bodySmall,
                color = QuakeTextMuted
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 6 Telemetry Metric Cards with Dynamic Waveforms (2 columns)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val metrics = telemetry.metrics
            val pairs = metrics.chunked(2)

            pairs.forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        TelemetryCard(
                            metric = pair[0],
                            isAnimated = telemetry.isLiveStreaming
                        )
                    }
                    if (pair.size > 1) {
                        Box(modifier = Modifier.weight(1f)) {
                            TelemetryCard(
                                metric = pair[1],
                                isAnimated = telemetry.isLiveStreaming
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
        HorizontalDivider(color = CarbonBorderSubtle)
        Spacer(modifier = Modifier.height(20.dp))

        // SYSTEM STATUS GRID (6 diagnostic items)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "DIAGNOSTIC MATRIX",
                style = MaterialTheme.typography.labelSmall,
                color = QuakePlasmaRed,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "SYSTEM STATUS",
                style = MaterialTheme.typography.titleLarge,
                color = QuakeTextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                telemetry.systemStatuses.forEach { item ->
                    StatusIndicator(item = item)
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
        HorizontalDivider(color = CarbonBorderSubtle)
        Spacer(modifier = Modifier.height(20.dp))

        // SCHEDULE SERVICE / MAINTENANCE
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "PROPULSION HEALTH",
                style = MaterialTheme.typography.labelSmall,
                color = QuakePlasmaRed,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "SCHEDULE SERVICE",
                style = MaterialTheme.typography.titleLarge,
                color = QuakeTextWhite
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(2.dp),
                colors = CardDefaults.cardColors(containerColor = CarbonSurface),
                border = BorderStroke(1.dp, CarbonBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Your Quantum Quake is performing optimally. Next recommended service in 4,200 km.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = QuakeTextMuted,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { showBookingSheet = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("book_appointment_button"),
                        shape = RoundedCornerShape(2.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = QuakeCobalt,
                            contentColor = QuakeTextWhite
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "BOOK APPOINTMENT",
                            style = MaterialTheme.typography.labelLarge,
                            letterSpacing = 1.5.sp
                        )
                    }
                }
            }

            // Confirmed Appointments List
            if (appointments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "CONFIRMED SESSIONS (${appointments.size})",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextMuted,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                appointments.forEach { appt ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(2.dp),
                        colors = CardDefaults.cardColors(containerColor = CarbonSurfaceVariant),
                        border = BorderStroke(1.dp, QuakeCobalt.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = appt.serviceType,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = QuakeTextWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${appt.centerName} · ${appt.centerLocation}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = QuakeTextMuted
                                )
                                Text(
                                    text = "${appt.dateString} at ${appt.timeSlot}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = QuakeCyan,
                                    letterSpacing = 1.sp
                                )
                            }

                            IconButton(
                                onClick = { viewModel.cancelAppointment(appt.id) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Cancel Appointment",
                                    tint = QuakeTextFaint
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
        HorizontalDivider(color = CarbonBorderSubtle)
        Spacer(modifier = Modifier.height(20.dp))

        // CONNECTED VEHICLE & SYSTEM PREFERENCES
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "TELEMETRY LINK",
                style = MaterialTheme.typography.labelSmall,
                color = QuakePlasmaRed,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "CONNECTED VEHICLE",
                style = MaterialTheme.typography.titleLarge,
                color = QuakeTextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(2.dp),
                colors = CardDefaults.cardColors(containerColor = CarbonSurface),
                border = BorderStroke(1.dp, CarbonBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Vehicle VIN Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "VIN / CHASSIS NUMBER",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuakeTextFaint,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = connectedVin,
                                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                                color = QuakeTextWhite,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        IconButton(onClick = {
                            vinInput = connectedVin
                            showVinDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit VIN",
                                tint = QuakeTextMuted
                            )
                        }
                    }

                    HorizontalDivider(color = CarbonBorderSubtle)

                    // Service Alerts Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Service Diagnostic Alerts",
                                style = MaterialTheme.typography.bodyMedium,
                                color = QuakeTextWhite
                            )
                            Text(
                                text = "Proactive maintenance push notifications",
                                style = MaterialTheme.typography.bodySmall,
                                color = QuakeTextMuted
                            )
                        }

                        Switch(
                            checked = serviceAlertsEnabled,
                            onCheckedChange = { viewModel.toggleServiceAlerts() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = QuakeTextWhite,
                                checkedTrackColor = QuakeCobalt,
                                uncheckedTrackColor = CarbonBorder
                            )
                        )
                    }

                    HorizontalDivider(color = CarbonBorderSubtle)

                    // Seismic Haptic Feedback
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Seismic Haptic Resonance",
                                style = MaterialTheme.typography.bodyMedium,
                                color = QuakeTextWhite
                            )
                            Text(
                                text = "Tactile impulse vibration on throttle peaks",
                                style = MaterialTheme.typography.bodySmall,
                                color = QuakeTextMuted
                            )
                        }

                        Switch(
                            checked = seismicHapticsEnabled,
                            onCheckedChange = { seismicHapticsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = QuakeTextWhite,
                                checkedTrackColor = QuakePlasmaRed,
                                uncheckedTrackColor = CarbonBorder
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }

    // Book Service Appointment Bottom Sheet
    if (showBookingSheet) {
        var selectedCenter by remember { mutableStateOf("Zurich Quantum Hub") }
        var selectedLocation by remember { mutableStateOf("Switzerland") }
        var selectedDate by remember { mutableStateOf("Oct 24, 2026") }
        var selectedTime by remember { mutableStateOf("10:00 AM") }
        var selectedType by remember { mutableStateOf("Annual Diagnostics & Calibration") }

        val serviceCenters = listOf(
            "Zurich Quantum Hub" to "Switzerland",
            "San Francisco Motion Lab" to "California, USA",
            "Tokyo Propulsion Lab" to "Aoyama, Japan",
            "Berlin Megacenter" to "Germany"
        )

        val serviceTypes = listOf(
            "Annual Diagnostics & Calibration",
            "Track Performance Inspection",
            "Battery Cell Optimization",
            "Brake & Fluid Service"
        )

        val timeSlots = listOf("09:00 AM", "11:30 AM", "02:00 PM", "04:30 PM")

        ModalBottomSheet(
            onDismissRequest = { showBookingSheet = false },
            sheetState = sheetState,
            containerColor = CarbonBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "SCHEDULE SERVICE",
                    style = MaterialTheme.typography.headlineSmall,
                    color = QuakeTextWhite,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Book an official Quantum Quake engineering consultation",
                    style = MaterialTheme.typography.bodySmall,
                    color = QuakeTextMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Service Center Selection
                Text(
                    text = "SELECT SERVICE CENTER",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakePlasmaRed,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                serviceCenters.forEach { (name, loc) ->
                    val isSelected = selectedCenter == name
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clickable {
                                selectedCenter = name
                                selectedLocation = loc
                            },
                        shape = RoundedCornerShape(2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) CarbonSurfaceVariant else CarbonSurface
                        ),
                        border = BorderStroke(1.dp, if (isSelected) QuakeCobalt else CarbonBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = QuakeTextWhite,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = loc,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = QuakeTextMuted
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = QuakeCobalt,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Service Type Selection
                Text(
                    text = "SERVICE TYPE",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakePlasmaRed,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                serviceTypes.forEach { type ->
                    val isSelected = selectedType == type
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clickable { selectedType = type },
                        shape = RoundedCornerShape(2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) CarbonSurfaceVariant else CarbonSurface
                        ),
                        border = BorderStroke(1.dp, if (isSelected) QuakeCobalt else CarbonBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = type,
                                style = MaterialTheme.typography.bodyMedium,
                                color = QuakeTextWhite
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = QuakeCobalt,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Time Slot Selection
                Text(
                    text = "AVAILABLE TIME SLOT",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakePlasmaRed,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    timeSlots.forEach { slot ->
                        val isSelected = selectedTime == slot
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (isSelected) QuakeCobalt.copy(alpha = 0.2f) else CarbonSurface,
                                    RoundedCornerShape(2.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) QuakeCobalt else CarbonBorder,
                                    RoundedCornerShape(2.dp)
                                )
                                .clickable { selectedTime = slot }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = slot,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) QuakeTextWhite else QuakeTextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.bookAppointment(
                            centerName = selectedCenter,
                            centerLocation = selectedLocation,
                            dateString = selectedDate,
                            timeSlot = selectedTime,
                            serviceType = selectedType
                        )
                        coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                            showBookingSheet = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_booking_button"),
                    shape = RoundedCornerShape(2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = QuakeCobalt,
                        contentColor = QuakeTextWhite
                    )
                ) {
                    Text(
                        text = "CONFIRM RESERVATION",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        letterSpacing = 2.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Edit VIN Dialog
    if (showVinDialog) {
        AlertDialog(
            onDismissRequest = { showVinDialog = false },
            title = {
                Text(
                    text = "UPDATE CONNECTED VIN",
                    style = MaterialTheme.typography.titleMedium,
                    color = QuakeTextWhite,
                    letterSpacing = 1.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter the 17-character chassis identification code from your vehicle door sill or dashboard.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuakeTextMuted
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = vinInput,
                        onValueChange = { vinInput = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateVehicleVin(vinInput)
                    showVinDialog = false
                }) {
                    Text("SAVE", color = QuakeCobalt)
                }
            },
            dismissButton = {
                TextButton(onClick = { showVinDialog = false }) {
                    Text("CANCEL", color = QuakeTextMuted)
                }
            },
            containerColor = CarbonSurface
        )
    }
}
