package com.cjanie.voltagedropcalculator.businesslogic.models.use

import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionnalContext
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.PhaseShift

class Lighting(electricitySupply: ElectricitySupply): Use(electricitySupply) {
    override val functionalContext: FunctionnalContext
        get() = FunctionnalContext.LIGHTING
    override val phaseShift: PhaseShift
        get() = PhaseShift(cosPHI = 1f)

    override fun maxVoltageDropAcceptablePercentage(): Float {
        return when (electricitySupply) {
            ElectricitySupply.PUBLIC -> 3f
            ElectricitySupply.PRIVATE -> 6f
        }
    }

}