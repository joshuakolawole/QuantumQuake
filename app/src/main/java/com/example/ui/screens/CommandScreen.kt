package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.SpecNumberCard
import com.example.ui.theme.CarbonBackground
import com.example.ui.theme.CarbonBorder
import com.example.ui.theme.CarbonBorderSubtle
import com.example.ui.theme.CarbonSurface
import com.example.ui.theme.QuakeCobalt
import com.example.ui.theme.QuakeCyan
import com.example.ui.theme.QuakePlasmaRed
import com.example.ui.theme.QuakeTextFaint
import com.example.ui.theme.QuakeTextMuted
import com.example.ui.theme.QuakeTextWhite

@Composable
fun CommandScreen(
    onNavigateToConfigurator: () -> Unit,
    onNavigateToPulseGrid: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedBeamMode by remember { mutableStateOf("Adaptive Matrix") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CarbonBackground)
            .verticalScroll(scrollState)
            .testTag("command_screen")
    ) {
        // Top telemetry status HUD banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CarbonSurface)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SYS.STATUS: NOMINAL",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "•",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextFaint
                )
                Text(
                    text = "PWR.OUTPUT: 847 kW",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakePlasmaRed,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "•",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextFaint
                )
                Text(
                    text = "TORQUE.VEC: 1,200 Nm",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextMuted,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "•",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextFaint
                )
                Text(
                    text = "AERO.COEFF: 0.21 Cd",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextMuted,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Hero Header & Title
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "MODEL YEAR 2026",
                style = MaterialTheme.typography.labelMedium,
                color = QuakePlasmaRed,
                letterSpacing = 2.5.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "THE",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black
                ),
                color = QuakeTextWhite,
                lineHeight = 42.sp
            )
            Text(
                text = "QUAKE",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black
                ),
                color = QuakePlasmaRed,
                lineHeight = 44.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Showcase Image with Salt Flats
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, CarbonBorder)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.hero_vehicle),
                        contentDescription = "Quantum Quake vehicle on salt flats",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    )

                    // Bottom dark gradient overlay
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        CarbonBackground.copy(alpha = 0.85f),
                                        CarbonBackground
                                    )
                                )
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Call to action & quick specs summary
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Button(
                onClick = onNavigateToConfigurator,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("hero_experience_button"),
                shape = RoundedCornerShape(2.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = QuakePlasmaRed,
                    contentColor = QuakeTextWhite
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "EXPERIENCE THE QUAKE",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle spec ticker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "0–100 km/h in 2.1s",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextWhite,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "  ·  ",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextFaint
                )
                Text(
                    text = "847 kW peak",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextWhite,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "  ·  ",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextFaint
                )
                Text(
                    text = "680 km range",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextWhite,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
        HorizontalDivider(color = CarbonBorderSubtle, thickness = 1.dp)
        Spacer(modifier = Modifier.height(28.dp))

        // RAW NUMBERS / PERFORMANCE DATA
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "PERFORMANCE DATA",
                style = MaterialTheme.typography.labelSmall,
                color = QuakePlasmaRed,
                letterSpacing = 2.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "RAW NUMBERS",
                style = MaterialTheme.typography.headlineMedium,
                color = QuakeTextWhite
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2-column grid of spec cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SpecNumberCard(label = "Peak Power", value = "847", unit = "kW")
                }
                Box(modifier = Modifier.weight(1f)) {
                    SpecNumberCard(label = "0–100 km/h", value = "2.1", unit = "sec")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SpecNumberCard(label = "Max Range", value = "680", unit = "km")
                }
                Box(modifier = Modifier.weight(1f)) {
                    SpecNumberCard(label = "Max Torque", value = "1,200", unit = "Nm")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SpecNumberCard(label = "Top Speed", value = "340", unit = "km/h")
                }
                Box(modifier = Modifier.weight(1f)) {
                    SpecNumberCard(label = "Aero Coeff", value = "0.21", unit = "Cd")
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
        HorizontalDivider(color = CarbonBorderSubtle, thickness = 1.dp)
        Spacer(modifier = Modifier.height(28.dp))

        // ENGINEERED LIGHT / PHOTON MATRIX ILLUMINATION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "ENGINEERED LIGHT",
                style = MaterialTheme.typography.labelSmall,
                color = QuakePlasmaRed,
                letterSpacing = 2.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "PHOTON MATRIX\nILLUMINATION",
                style = MaterialTheme.typography.headlineMedium,
                color = QuakeTextWhite,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, CarbonBorder)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.headlight_detail),
                        contentDescription = "Quantum Quake headlight macro detail",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )

                    // Subtle cyan illumination aura
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        CarbonBackground.copy(alpha = 0.9f)
                                    )
                                )
                            )
                    )

                    // Matrix indicator badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(
                                CarbonSurface.copy(alpha = 0.85f),
                                RoundedCornerShape(2.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Highlight,
                                contentDescription = null,
                                tint = QuakeCyan,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "2,048 MICRO-LEDS",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuakeCyan,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Each headlight contains 2,048 individually addressable micro-LEDs, sculpting light with quantum precision. The adaptive beam traces corners before you turn the wheel.",
                style = MaterialTheme.typography.bodyMedium,
                color = QuakeTextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Light Matrix Beam Mode Selector
            Text(
                text = "INTERACTIVE BEAM SIMULATOR:",
                style = MaterialTheme.typography.labelSmall,
                color = QuakeTextFaint,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Adaptive Matrix", "High Beam Sweep", "Corner Tracking").forEach { mode ->
                    val isSelected = selectedBeamMode == mode
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) QuakePlasmaRed.copy(alpha = 0.15f) else CarbonSurface,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) QuakePlasmaRed else CarbonBorderSubtle,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .clickable { selectedBeamMode = mode }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = mode.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) QuakePlasmaRed else QuakeTextMuted,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
        HorizontalDivider(color = CarbonBorderSubtle, thickness = 1.dp)
        Spacer(modifier = Modifier.height(28.dp))

        // THE COCKPIT / COMMAND YOUR REALITY
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "THE COCKPIT",
                style = MaterialTheme.typography.labelSmall,
                color = QuakePlasmaRed,
                letterSpacing = 2.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "COMMAND\nYOUR REALITY",
                style = MaterialTheme.typography.headlineMedium,
                color = QuakeTextWhite,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, CarbonBorder)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cockpit_interior),
                    contentDescription = "Quantum Quake interior cockpit",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "A driver-centric architecture where every surface responds to intent. Ambient intelligence anticipates your next move.",
                style = MaterialTheme.typography.bodyMedium,
                color = QuakeTextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onNavigateToConfigurator,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("cockpit_build_button"),
                shape = RoundedCornerShape(2.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = QuakeTextWhite
                ),
                border = BorderStroke(1.dp, QuakeTextWhite.copy(alpha = 0.4f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "BUILD YOURS",
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Footer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CarbonSurface)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "© 2026 QUANTUM QUAKE",
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextFaint,
                    letterSpacing = 1.5.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "PULSE GRID",
                        style = MaterialTheme.typography.labelSmall,
                        color = QuakeCobalt,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.clickable { onNavigateToPulseGrid() }
                    )
                    Text(
                        text = "CONFIGURATOR",
                        style = MaterialTheme.typography.labelSmall,
                        color = QuakePlasmaRed,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.clickable { onNavigateToConfigurator() }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
