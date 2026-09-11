package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.sin

@Composable
fun QuakeWaveform(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF0057FF),
    intensity: Float = 1.0f,
    phaseOffset: Float = 0f,
    isAnimated: Boolean = true
) {
    val transition = rememberInfiniteTransition(label = "wave_anim")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (2400 / intensity.coerceAtLeast(0.5f)).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        if (width <= 0f || height <= 0f) return@Canvas

        val samplePoints = 48
        val strokePath = Path()
        val fillPath = Path()

        val midY = height * 0.5f
        val maxAmplitude = height * 0.38f * intensity.coerceIn(0.2f, 1.4f)
        val currentPhase = if (isAnimated) phase + phaseOffset else phaseOffset

        for (i in 0..samplePoints) {
            val progress = i.toFloat() / samplePoints.toFloat()
            val x = progress * width

            // Seismic oscillation with multi-frequency harmonic modulation
            val primaryAngle = progress * 4f * Math.PI.toFloat() + currentPhase
            val secondaryAngle = progress * 9f * Math.PI.toFloat() - (currentPhase * 1.5f)
            val tertiaryAngle = progress * 15f * Math.PI.toFloat() + (currentPhase * 0.7f)

            val yOffset = (sin(primaryAngle) * 0.65f +
                    sin(secondaryAngle) * 0.25f +
                    sin(tertiaryAngle) * 0.10f) * maxAmplitude

            val y = (midY + yOffset).coerceIn(4f, height - 4f)

            if (i == 0) {
                strokePath.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                strokePath.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
        }

        fillPath.lineTo(width, height)
        fillPath.close()

        // Draw gradient area under seismic waveform
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color.copy(alpha = 0.35f),
                    color.copy(alpha = 0.08f),
                    Color.Transparent
                ),
                startY = 0f,
                endY = height
            )
        )

        // Draw precision stroke
        drawPath(
            path = strokePath,
            color = color,
            style = Stroke(
                width = 1.8.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
