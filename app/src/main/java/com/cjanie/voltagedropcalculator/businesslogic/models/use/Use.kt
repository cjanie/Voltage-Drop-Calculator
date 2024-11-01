package com.cjanie.voltagedropcalculator.businesslogic.models.use

import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionalContext
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.PhaseShift

abstract class Use(val electricitySupply: ElectricitySupply){
    abstract val usage: FunctionalContext
    abstract val phaseShift: PhaseShift
    abstract fun maxVoltageDropAcceptablePercentage(): Float
}