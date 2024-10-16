package com.cjanie.voltagedropcalculator.businesslogic.models.use

import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.PhaseShift

class Lighting(private val electricitySupply: ElectricitySupply): Use() {
    override val phaseShift: PhaseShift
        get() = PhaseShift(cosPHI = 1f)

    override fun maxVoltageDropAcceptablePercentage(): Float {
        return when (electricitySupply) {
            ElectricitySupply.PUBLIC -> 3f
            ElectricitySupply.PRIVATE -> 6f
        }
    }

}