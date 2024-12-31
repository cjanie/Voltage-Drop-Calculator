package com.cjanie.voltagedropcalculator.businesslogic.voltagedrop

import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.ConductorVoltageDropProperty
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop

class Line(
    val length: Length,
    val conductorMaterial: ConductorMaterial = ConductorMaterial.COPPER,
    val section: Section,
    val intensity: Intensity
) {
    companion object {
        fun voltageDrop(K: ConductorVoltageDropProperty, I: Intensity, L: Length): VoltageDrop {
            return VoltageDrop(inVolt =
            K.inVoltPerAmpereAndPerKilometer * I.inAmpere * L.inKilometer
            )
        }
    }

    fun voltageDrop(): VoltageDrop {
        val K = ConductorVoltageDropProperty.property(
            conductorMaterial = conductorMaterial,
            section = section
        )
        return Companion.voltageDrop(K, intensity, length)
    }
}