package com.example.data.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.ui.theme.PaintArcticWhite
import com.example.ui.theme.PaintPlasmaRed
import com.example.ui.theme.PaintQuantumCobalt
import com.example.ui.theme.PaintSolarAmber
import com.example.ui.theme.PaintStealthMatte
import com.example.ui.theme.PaintVoidEmerald
import com.example.ui.theme.WheelGlossBlack
import com.example.ui.theme.WheelMatteCarbon
import com.example.ui.theme.WheelSatinSilver
import kotlin.random.Random

enum class BodyStyle(
    val id: String,
    val modelName: String,
    val displayName: String,
    val tagline: String,
    @DrawableRes val exteriorRes: Int,
    @DrawableRes val interiorRes: Int = R.drawable.cockpit_interior
) {
    COUPE("coupe", "QQ-C", "Coupe", "Pure Aggression", R.drawable.coupe_exterior),
    SEDAN("sedan", "QQ-S", "Sedan", "Refined Velocity", R.drawable.sedan_exterior),
    SUV("suv", "QQ-X", "SUV", "Elevated Dominance", R.drawable.suv_exterior);

    companion object {
        fun fromId(id: String): BodyStyle = entries.firstOrNull { it.id == id } ?: COUPE
    }
}

enum class ExteriorColor(
    val id: String,
    val displayName: String,
    val color: Color,
    val hexCode: String
) {
    PLASMA("plasma", "Plasma Red", PaintPlasmaRed, "#FF2200"),
    STEALTH("stealth", "Stealth Matte", PaintStealthMatte, "#1A1A2E"),
    SOLAR("solar", "Solar Amber", PaintSolarAmber, "#FF8C00"),
    COBALT("cobalt", "Quantum Cobalt", PaintQuantumCobalt, "#0057FF"),
    ARCTIC("arctic", "Arctic White", PaintArcticWhite, "#F2F4F7"),
    EMERALD("emerald", "Void Emerald", PaintVoidEmerald, "#00A86B");

    companion object {
        fun fromId(id: String): ExteriorColor = entries.firstOrNull { it.id == id } ?: PLASMA
    }
}

enum class InteriorTrim(
    val id: String,
    val displayName: String,
    val description: String
) {
    ONYX("onyx", "Onyx Leather", "Semi-aniline perforated black leather with laser-etched stitching"),
    GLACIER("glacier", "Glacier Alcantara", "Lightweight synthetic micro-suede engineered for high G-forces"),
    CARBON("carbon", "Exposed Carbon", "Matte weave autoclave composite structural shell seating");

    companion object {
        fun fromId(id: String): InteriorTrim = entries.firstOrNull { it.id == id } ?: ONYX
    }
}

enum class WheelOption(
    val id: String,
    val sizeInch: Int,
    val displayName: String,
    val finishLabel: String,
    val color: Color
) {
    WHEEL_19("19", 19, "19\" Pulse", "Gloss Black", WheelGlossBlack),
    WHEEL_21("21", 21, "21\" Orbital", "Satin Silver", WheelSatinSilver),
    WHEEL_23("23", 23, "23\" Quake Forged", "Matte Carbon", WheelMatteCarbon);

    companion object {
        fun fromId(id: String): WheelOption = entries.firstOrNull { it.id == id } ?: WHEEL_21
    }
}

enum class DrivetrainOption(
    val id: String,
    val displayName: String,
    val peakPowerKw: Int,
    val torqueNm: Int,
    val acceleration0100: Double,
    val topSpeedKmh: Int,
    val rangeKm: Int
) {
    DUAL("dual", "Dual Motor AWD", 847, 1200, 2.1, 340, 680),
    TRI("tri", "Tri Motor — Track", 1050, 1500, 1.9, 360, 620);

    companion object {
        fun fromId(id: String): DrivetrainOption = entries.firstOrNull { it.id == id } ?: DUAL
    }
}

data class ActiveVehicleConfig(
    val bodyStyle: BodyStyle = BodyStyle.COUPE,
    val exteriorColor: ExteriorColor = ExteriorColor.PLASMA,
    val interiorTrim: InteriorTrim = InteriorTrim.ONYX,
    val wheelOption: WheelOption = WheelOption.WHEEL_21,
    val drivetrain: DrivetrainOption = DrivetrainOption.DUAL,
    val viewMode: ViewMode = ViewMode.EXTERIOR,
    val generatedQuantumId: String? = null
) {
    enum class ViewMode { EXTERIOR, INTERIOR }
}

fun generateQuantumId(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val randomPart = (1..8)
        .map { chars[Random.nextInt(chars.length)] }
        .joinToString("")
    return "QQ-$randomPart"
}
