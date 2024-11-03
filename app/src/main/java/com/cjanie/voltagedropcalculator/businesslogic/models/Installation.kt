package com.cjanie.voltagedropcalculator.businesslogic.models

import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Motor

abstract class Installation : CalculateVoltageDrop {

    companion object {
        fun isPhaseShiftConsistent(cable: Line, usage: Usage, electricitySupply: ElectricitySupply): Boolean {
            val use = when(usage) {
                Usage.MOTOR -> Motor(electricitySupply)
                Usage.LIGHTING -> Lighting(electricitySupply)
            }
            return cable.phaseShift == use.phaseShift

        }
    }
}