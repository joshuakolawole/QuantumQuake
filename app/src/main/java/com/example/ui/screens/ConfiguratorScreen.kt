package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.SavedConfigEntity
import com.example.data.model.ActiveVehicleConfig
import com.example.data.model.BodyStyle
import com.example.data.model.DrivetrainOption
import com.example.data.model.ExteriorColor
import com.example.data.model.InteriorTrim
import com.example.data.model.WheelOption
import com.example.ui.theme.CarbonBackground
import com.example.ui.theme.CarbonBorder
import com.example.ui.theme.CarbonBorderSubtle
import com.example.ui.theme.CarbonSurface
import com.example.ui.theme.CarbonSurfaceVariant
import com.example.ui.theme.QuakeCobalt
import com.example.ui.theme.QuakeCyan
import com.example.ui.theme.QuakeEmerald
import com.example.ui.theme.QuakePlasmaRed
import com.example.ui.theme.QuakeTextFaint
import com.example.ui.theme.QuakeTextMuted
import com.example.ui.theme.QuakeTextWhite
import com.example.ui.viewmodel.QuakeViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguratorScreen(
    viewModel: QuakeViewModel,
    modifier: Modifier = Modifier
) {
    val activeConfig by viewModel.activeConfig.collectAsState()
    val savedConfigs by viewModel.savedConfigs.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showGarageSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonBackground)
            .verticalScroll(scrollState)
            .testTag("configurator_screen")
    ) {
        // Top HUD bar with active config summary & Garage quick button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CarbonSurface)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "STUDIO CONFIGURATOR",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakePlasmaRed,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "${activeConfig.bodyStyle.modelName} // ${activeConfig.exteriorColor.displayName.uppercase()}",
                    style = MaterialTheme.typography.labelMedium,
                    color = QuakeTextWhite,
                    letterSpacing = 1.sp
                )
            }

            OutlinedButton(
                onClick = { showGarageSheet = true },
                shape = RoundedCornerShape(2.dp),
                border = BorderStroke(1.dp, CarbonBorder),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = QuakeTextWhite),
                modifier = Modifier.testTag("garage_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Garage,
                    contentDescription = "My Garage",
                    tint = QuakeCobalt,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "GARAGE (${savedConfigs.size})",
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 1.sp
                )
            }
        }

        // View Mode Switch Tabs (Exterior vs Interior)
        TabRow(
            selectedTabIndex = if (activeConfig.viewMode == ActiveVehicleConfig.ViewMode.EXTERIOR) 0 else 1,
            containerColor = CarbonSurface,
            contentColor = QuakeTextWhite,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        tabPositions[if (activeConfig.viewMode == ActiveVehicleConfig.ViewMode.EXTERIOR) 0 else 1]
                    ),
                    color = QuakePlasmaRed
                )
            },
            divider = { HorizontalDivider(color = CarbonBorderSubtle) }
        ) {
            Tab(
                selected = activeConfig.viewMode == ActiveVehicleConfig.ViewMode.EXTERIOR,
                onClick = { viewModel.setViewMode(ActiveVehicleConfig.ViewMode.EXTERIOR) },
                text = {
                    Text(
                        text = "EXTERIOR STUDIO",
                        style = MaterialTheme.typography.labelMedium,
                        letterSpacing = 1.5.sp
                    )
                }
            )
            Tab(
                selected = activeConfig.viewMode == ActiveVehicleConfig.ViewMode.INTERIOR,
                onClick = { viewModel.setViewMode(ActiveVehicleConfig.ViewMode.INTERIOR) },
                text = {
                    Text(
                        text = "INTERIOR COCKPIT",
                        style = MaterialTheme.typography.labelMedium,
                        letterSpacing = 1.5.sp
                    )
                }
            )
        }

        // Studio Viewport (Vehicle rendering with active paint tint overlay or interior)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(CarbonSurfaceVariant)
        ) {
            AnimatedContent(
                targetState = activeConfig.viewMode,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "viewport_anim"
            ) { mode ->
                if (mode == ActiveVehicleConfig.ViewMode.EXTERIOR) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = activeConfig.bodyStyle.exteriorRes),
                            contentDescription = "${activeConfig.bodyStyle.displayName} exterior",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Subtle atmospheric paint tone highlight
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    activeConfig.exteriorColor.color.copy(alpha = 0.07f)
                                )
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.cockpit_interior),
                            contentDescription = "Cockpit interior",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Overlay HUD Badge: Active Model, Paint, and Drivetrain
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .background(
                        CarbonBackground.copy(alpha = 0.85f),
                        RoundedCornerShape(2.dp)
                    )
                    .border(1.dp, CarbonBorderSubtle, RoundedCornerShape(2.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(activeConfig.exteriorColor.color)
                    )
                    Text(
                        text = "${activeConfig.bodyStyle.modelName} · ${activeConfig.wheelOption.displayName} · ${activeConfig.drivetrain.displayName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = QuakeTextWhite,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECTION 1: BODY STYLE (Coupe, Sedan, SUV)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "01 // BODY STYLE",
                style = MaterialTheme.typography.labelSmall,
                color = QuakePlasmaRed,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BodyStyle.entries.forEach { style ->
                    val isSelected = activeConfig.bodyStyle == style
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.setBodyStyle(style) }
                            .testTag("body_style_${style.id}"),
                        shape = RoundedCornerShape(2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) CarbonSurfaceVariant else CarbonSurface
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) QuakePlasmaRed else CarbonBorder
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = style.modelName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) QuakePlasmaRed else QuakeTextWhite
                            )
                            Text(
                                text = style.displayName,
                                style = MaterialTheme.typography.bodySmall,
                                color = QuakeTextWhite
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = style.tagline,
                                style = MaterialTheme.typography.labelSmall,
                                color = QuakeTextMuted,
                                fontSize = 9.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = CarbonBorderSubtle)
        Spacer(modifier = Modifier.height(16.dp))

        // SECTION 2: EXTERIOR PAINT
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "02 // EXTERIOR PAINT",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakePlasmaRed,
                    letterSpacing = 2.sp
                )
                Text(
                    text = activeConfig.exteriorColor.displayName.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextWhite,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Paint color swatches
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExteriorColor.entries.forEach { paint ->
                    val isSelected = activeConfig.exteriorColor == paint
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { viewModel.setExteriorColor(paint) }
                            .testTag("paint_${paint.id}")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) QuakePlasmaRed else CarbonBorder,
                                    shape = CircleShape
                                )
                                .padding(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(paint.color)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = paint.displayName.split(" ").first(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) QuakeTextWhite else QuakeTextMuted,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = CarbonBorderSubtle)
        Spacer(modifier = Modifier.height(16.dp))

        // SECTION 3: INTERIOR TRIM
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "03 // INTERIOR TRIM",
                style = MaterialTheme.typography.labelSmall,
                color = QuakePlasmaRed,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            InteriorTrim.entries.forEach { trim ->
                val isSelected = activeConfig.interiorTrim == trim
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.setInteriorTrim(trim) }
                        .testTag("interior_${trim.id}"),
                    shape = RoundedCornerShape(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) CarbonSurfaceVariant else CarbonSurface
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) QuakePlasmaRed else CarbonBorder
                    )
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
                                text = trim.displayName.uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) QuakePlasmaRed else QuakeTextWhite,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = trim.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = QuakeTextMuted
                            )
                        }
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = QuakePlasmaRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = CarbonBorderSubtle)
        Spacer(modifier = Modifier.height(16.dp))

        // SECTION 4: WHEELS
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "04 // WHEELS",
                style = MaterialTheme.typography.labelSmall,
                color = QuakePlasmaRed,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WheelOption.entries.forEach { wheel ->
                    val isSelected = activeConfig.wheelOption == wheel
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.setWheelOption(wheel) }
                            .testTag("wheel_${wheel.id}"),
                        shape = RoundedCornerShape(2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) CarbonSurfaceVariant else CarbonSurface
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) QuakePlasmaRed else CarbonBorder
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(wheel.color)
                                    .border(1.dp, CarbonBorder, CircleShape)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = wheel.displayName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) QuakePlasmaRed else QuakeTextWhite
                            )
                            Text(
                                text = wheel.finishLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = QuakeTextMuted,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = CarbonBorderSubtle)
        Spacer(modifier = Modifier.height(16.dp))

        // SECTION 5: DRIVETRAIN
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "05 // PROPULSION DRIVETRAIN",
                style = MaterialTheme.typography.labelSmall,
                color = QuakePlasmaRed,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            DrivetrainOption.entries.forEach { drive ->
                val isSelected = activeConfig.drivetrain == drive
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.setDrivetrain(drive) }
                        .testTag("drivetrain_${drive.id}"),
                    shape = RoundedCornerShape(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) CarbonSurfaceVariant else CarbonSurface
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) QuakePlasmaRed else CarbonBorder
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = drive.displayName.uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) QuakePlasmaRed else QuakeTextWhite,
                                letterSpacing = 1.sp
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = QuakePlasmaRed,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${drive.peakPowerKw} kW peak",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuakeTextWhite
                            )
                            Text(
                                text = "${drive.torqueNm} Nm torque",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuakeTextWhite
                            )
                            Text(
                                text = "0–100 in ${drive.acceleration0100}s",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuakeCobalt
                            )
                            Text(
                                text = "${drive.rangeKm} km",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuakeTextMuted
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
        HorizontalDivider(color = CarbonBorderSubtle)
        Spacer(modifier = Modifier.height(20.dp))

        // SECTION 6: QUANTUM ID GENERATOR
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            val quantumId = activeConfig.generatedQuantumId

            if (quantumId == null) {
                Button(
                    onClick = { viewModel.generateNewQuantumId() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("generate_quantum_id_button"),
                    shape = RoundedCornerShape(2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = QuakePlasmaRed,
                        contentColor = QuakeTextWhite
                    )
                ) {
                    Text(
                        text = "GENERATE QUANTUM ID",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        letterSpacing = 2.sp
                    )
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(2.dp),
                    colors = CardDefaults.cardColors(containerColor = CarbonSurface),
                    border = BorderStroke(1.dp, QuakePlasmaRed)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "YOUR UNIQUE SPECIFICATION",
                            style = MaterialTheme.typography.labelSmall,
                            color = QuakeTextMuted,
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = quantumId,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black
                            ),
                            color = QuakePlasmaRed,
                            letterSpacing = 3.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Present this ID at your dealership or save to Garage.",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuakeTextMuted
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Quantum ID", quantumId)
                                    clipboard.setPrimaryClip(clip)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(2.dp),
                                border = BorderStroke(1.dp, CarbonBorder),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = QuakeTextWhite)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy ID",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "COPY",
                                    style = MaterialTheme.typography.labelSmall,
                                    letterSpacing = 1.sp
                                )
                            }

                            Button(
                                onClick = { viewModel.saveCurrentConfigToGarage() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(2.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = QuakeCobalt,
                                    contentColor = QuakeTextWhite
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = "Save Build",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "SAVE BUILD",
                                    style = MaterialTheme.typography.labelSmall,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "RECONFIGURE",
                            style = MaterialTheme.typography.labelSmall,
                            color = QuakeTextFaint,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier
                                .clickable { viewModel.resetQuantumId() }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }

    // Garage Modal Bottom Sheet
    if (showGarageSheet) {
        ModalBottomSheet(
            onDismissRequest = { showGarageSheet = false },
            sheetState = sheetState,
            containerColor = CarbonBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "QUANTUM GARAGE",
                        style = MaterialTheme.typography.headlineSmall,
                        color = QuakeTextWhite,
                        letterSpacing = 1.sp
                    )

                    if (savedConfigs.isNotEmpty()) {
                        Text(
                            text = "CLEAR ALL",
                            style = MaterialTheme.typography.labelSmall,
                            color = QuakePlasmaRed,
                            letterSpacing = 1.sp,
                            modifier = Modifier
                                .clickable { viewModel.clearAllSavedConfigs() }
                                .padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (savedConfigs.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Garage,
                                contentDescription = null,
                                tint = QuakeTextFaint,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No builds saved in garage yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = QuakeTextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Generate a Quantum ID and tap Save Build",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuakeTextFaint
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        savedConfigs.forEach { config ->
                            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            val dateStr = dateFormat.format(Date(config.timestamp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(2.dp),
                                colors = CardDefaults.cardColors(containerColor = CarbonSurface),
                                border = BorderStroke(1.dp, CarbonBorder)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = config.quantumId,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = QuakePlasmaRed,
                                            letterSpacing = 1.5.sp
                                        )
                                        Text(
                                            text = "${config.modelName} · ${config.exteriorColorName} · ${config.drivetrainId.uppercase()}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = QuakeTextWhite
                                        )
                                        Text(
                                            text = "Saved $dateStr",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = QuakeTextFaint,
                                            fontSize = 10.sp
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = {
                                                viewModel.loadSavedConfig(config)
                                                coroutineScope.launch { sheetState.hide() }
                                                    .invokeOnCompletion { showGarageSheet = false }
                                            },
                                            shape = RoundedCornerShape(2.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = CarbonSurfaceVariant,
                                                contentColor = QuakeTextWhite
                                            ),
                                            modifier = Modifier.height(36.dp)
                                        ) {
                                            Text(
                                                text = "LOAD",
                                                style = MaterialTheme.typography.labelSmall,
                                                letterSpacing = 1.sp
                                            )
                                        }

                                        IconButton(
                                            onClick = { viewModel.deleteSavedConfig(config.id) }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = QuakeTextFaint
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}
