package com.cjanie.voltagedropcalculator.businesslogic.voltagedrop

import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.ConductorVoltageDropProperty
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop

class Line(
    val length: Length,
    val conductorMaterial: ConductorMaterial,
    val section: Section,
    val intensity: Intensity,
    val usage: Usage
) {
    companion object {
        fun voltageDropAtEndOfLine(K: ConductorVoltageDropProperty, I: Intensity, L: Length): VoltageDrop {
            return VoltageDrop(inVolt =
            K.inVoltPerAmpereAndPerKilometer * I.inAmpere * L.inKilometer
            )
        }
    }

    fun voltageDropAtEndOfLine(): VoltageDrop {
        val K = ConductorVoltageDropProperty.property(
            conductorMaterial = conductorMaterial,
            section = section,
            usage = usage
        )
        return Companion.voltageDropAtEndOfLine(K, intensity, length)
    }
}