package com.cjanie.voltagedropcalculator.businesslogic.valueobjects

import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.Copper

class ConductorVoltageDropProperty(val inVoltPerAmpereAndPerKilometer: Float) {
    companion object {
        fun property(conductorMaterial: ConductorMaterial, section: Section): ConductorVoltageDropProperty {
            return when (conductorMaterial) {
                ConductorMaterial.COPPER -> Copper.voltageDropProperty(section = section)
            }
        }
    }
}
