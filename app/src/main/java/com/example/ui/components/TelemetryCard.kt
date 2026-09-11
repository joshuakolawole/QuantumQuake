package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MetricReading
import com.example.ui.theme.CarbonBorder
import com.example.ui.theme.CarbonSurface
import com.example.ui.theme.QuakeTextMuted
import com.example.ui.theme.QuakeTextWhite

@Composable
fun TelemetryCard(
    metric: MetricReading,
    modifier: Modifier = Modifier,
    isAnimated: Boolean = true
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("telemetry_card_${metric.id}"),
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = CarbonSurface
        ),
        border = BorderStroke(1.dp, CarbonBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = metric.title.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = QuakeTextMuted,
                    letterSpacing = 1.5.sp
                )

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = metric.value,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        ),
                        color = QuakeTextWhite,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = metric.unit,
                        style = MaterialTheme.typography.labelSmall,
                        color = QuakeTextMuted,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                QuakeWaveform(
                    color = metric.color,
                    intensity = metric.intensity,
                    phaseOffset = metric.phaseOffset,
                    isAnimated = isAnimated
                )
            }
        }
    }
}
