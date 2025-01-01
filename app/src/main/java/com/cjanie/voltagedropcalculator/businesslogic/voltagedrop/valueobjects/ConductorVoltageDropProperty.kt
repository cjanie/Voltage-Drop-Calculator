package com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects

import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.Copper

class ConductorVoltageDropProperty(val inVoltPerAmpereAndPerKilometer: Float) {
    companion object {
        fun property(conductorMaterial: ConductorMaterial, section: Section, usage: Usage): ConductorVoltageDropProperty {
            return when (conductorMaterial) {
                ConductorMaterial.COPPER -> Copper.voltageDropProperty(section = section, usage = usage)
            }
        }
    }
}
