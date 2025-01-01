package com.cjanie.voltagedropcalculator.businesslogic.voltagedrop

import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.ConductorVoltageDropProperty
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.VoltageDrop

class Line{
    companion object {
        fun voltageDropAtEndOfLine(K: ConductorVoltageDropProperty, I: Intensity, L: Length): VoltageDrop {
            return VoltageDrop(inVolt =
            K.inVoltPerAmpereAndPerKilometer * I.inAmpere * L.inKilometer
            )
        }
    }

}