package com.cjanie.voltagedropcalculator.businesslogic.voltagedrop

import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.ConductorVoltageDropProperty
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.VoltageDrop

class VoltageDropCalculator(conductorMaterial: ConductorMaterial, S: Section, usage: Usage, I: Intensity, L: Length) {

    val K = ConductorVoltageDropProperty.property(
            conductorMaterial = conductorMaterial,
            section = S,
            usage = usage
        )

    val voltageDropAtEndOfLine = VoltageDrop.voltageDropAtEndOfLine(K, I, L)

}