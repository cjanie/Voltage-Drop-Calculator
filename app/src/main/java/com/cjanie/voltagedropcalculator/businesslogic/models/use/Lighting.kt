package com.cjanie.voltagedropcalculator.businesslogic.models.use

import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.PhaseShift

class Lighting(electricitySupply: ElectricitySupply): Use(electricitySupply) {
    override val usage: Usage
        get() = Usage.LIGHTING
    override val phaseShift: PhaseShift
        get() = PhaseShift(cosPHI = 1f)

    override fun maxVoltageDropAcceptablePercentage(): Float {
        return when (electricitySupply) {
            ElectricitySupply.PUBLIC -> 3f
            ElectricitySupply.PRIVATE -> 6f
        }
    }

}