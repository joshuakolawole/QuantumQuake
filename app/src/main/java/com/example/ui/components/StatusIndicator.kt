package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SystemStatusItem
import com.example.ui.theme.CarbonBorderSubtle
import com.example.ui.theme.CarbonSurface
import com.example.ui.theme.QuakeAmber
import com.example.ui.theme.QuakeCobalt
import com.example.ui.theme.QuakeEmerald
import com.example.ui.theme.QuakeTextMuted
import com.example.ui.theme.QuakeTextWhite

@Composable
fun StatusIndicator(
    item: SystemStatusItem,
    modifier: Modifier = Modifier
) {
    val icon: ImageVector = when (item.iconName) {
        "battery" -> Icons.Default.BatteryChargingFull
        "tire" -> Icons.Default.Speed
        "brake" -> Icons.Default.Thermostat
        "charging" -> Icons.Default.ElectricCar
        "gps" -> Icons.Default.GpsFixed
        else -> Icons.Default.Build
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("status_item_${item.id}"),
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = CarbonSurface
        ),
        border = BorderStroke(1.dp, CarbonBorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = item.label,
                tint = QuakeTextMuted,
                modifier = Modifier.size(20.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = QuakeTextWhite
                )
                Text(
                    text = item.status,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (item.isOk) QuakeCobalt else QuakeAmber,
                    letterSpacing = 0.5.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (item.isOk) QuakeEmerald else QuakeAmber,
                        shape = CircleShape
                    )
            )
        }
    }
}
