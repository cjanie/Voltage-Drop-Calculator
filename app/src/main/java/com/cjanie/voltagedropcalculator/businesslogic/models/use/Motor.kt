package com.cjanie.voltagedropcalculator.businesslogic.models.use

import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionalContext
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.PhaseShift

class Motor(electricitySupply: ElectricitySupply): Use(electricitySupply) {
    override val usage: FunctionalContext
        get() = FunctionalContext.MOTOR
    override val phaseShift: PhaseShift
        get() = PhaseShift(cosPHI = 0.8f)

    override fun maxVoltageDropAcceptablePercentage(): Float {
        return when (electricitySupply) {
            ElectricitySupply.PUBLIC -> 5f
            ElectricitySupply.PRIVATE -> 8f
        }
    }
}