package com.cjanie.voltagedropcalculator.businesslogic.models.use

import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionnalContext
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.PhaseShift

abstract class Use(val electricitySupply: ElectricitySupply){
    abstract val functionalContext: FunctionnalContext
    abstract val phaseShift: PhaseShift
    abstract fun maxVoltageDropAcceptablePercentage(): Float
}