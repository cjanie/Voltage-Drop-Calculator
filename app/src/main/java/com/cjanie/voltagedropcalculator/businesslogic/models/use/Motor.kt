package com.cjanie.voltagedropcalculator.businesslogic.models.use

import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.PhaseShift

class Motor(electricitySupply: ElectricitySupply): Use(electricitySupply) {
    override val usage: Usage
        get() = Usage.MOTOR
    override val phaseShift: PhaseShift
        get() = PhaseShift(cosPHI = 0.8f)

    override fun maxVoltageDropAcceptablePercentage(): Float {
        return when (electricitySupply) {
            ElectricitySupply.PUBLIC -> 5f
            ElectricitySupply.PRIVATE -> 8f
        }
    }
}