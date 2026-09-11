package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.CommandScreen
import com.example.ui.screens.ConfiguratorScreen
import com.example.ui.screens.PulseGridScreen
import com.example.ui.theme.CarbonBackground
import com.example.ui.theme.CarbonBorder
import com.example.ui.theme.CarbonSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.QuakeCobalt
import com.example.ui.theme.QuakeEmerald
import com.example.ui.theme.QuakePlasmaRed
import com.example.ui.theme.QuakeTextFaint
import com.example.ui.theme.QuakeTextMuted
import com.example.ui.theme.QuakeTextWhite
import com.example.ui.viewmodel.QuakeViewModel

enum class QuakeNavTab(val label: String, val icon: ImageVector) {
    COMMAND("COMMAND", Icons.Default.Speed),
    CONFIGURE("CONFIGURE", Icons.Default.Tune),
    PULSE("PULSE", Icons.Default.ElectricBolt)
}

class MainActivity : ComponentActivity() {
    private val viewModel: QuakeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                QuantumQuakeApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun QuantumQuakeApp(viewModel: QuakeViewModel) {
    var currentTab by remember { mutableStateOf(QuakeNavTab.COMMAND) }
    val feedbackMessage by viewModel.userFeedbackMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonBackground),
        containerColor = CarbonBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CarbonBackground)
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.quake_logo),
                            contentDescription = "Quantum Quake emblem",
                            modifier = Modifier.size(28.dp)
                        )

                        Column {
                            Text(
                                text = "QUANTUM QUAKE",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black
                                ),
                                color = QuakeTextWhite,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = "MOTION LAB // M.Y. 2026",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuakeTextMuted,
                                fontSize = 9.sp,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }

                    // Pulse Online Pill
                    Row(
                        modifier = Modifier
                            .background(CarbonSurface, RoundedCornerShape(2.dp))
                            .border(1.dp, CarbonBorder, RoundedCornerShape(2.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(QuakeEmerald)
                        )
                        Text(
                            text = "SYNCED",
                            style = MaterialTheme.typography.labelSmall,
                            color = QuakeTextWhite,
                            fontSize = 9.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = CarbonSurface,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, CarbonBorder))
            ) {
                QuakeNavTab.entries.forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                letterSpacing = 1.5.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = QuakePlasmaRed,
                            selectedTextColor = QuakePlasmaRed,
                            indicatorColor = QuakePlasmaRed.copy(alpha = 0.12f),
                            unselectedIconColor = QuakeTextMuted,
                            unselectedTextColor = QuakeTextMuted
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentTab,
            label = "tab_crossfade",
            modifier = Modifier.padding(innerPadding)
        ) { tab ->
            when (tab) {
                QuakeNavTab.COMMAND -> CommandScreen(
                    onNavigateToConfigurator = { currentTab = QuakeNavTab.CONFIGURE },
                    onNavigateToPulseGrid = { currentTab = QuakeNavTab.PULSE }
                )
                QuakeNavTab.CONFIGURE -> ConfiguratorScreen(viewModel = viewModel)
                QuakeNavTab.PULSE -> PulseGridScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("Android") }
}

