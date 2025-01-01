package com.cjanie.voltagedropcalculator.businesslogic.models.use

import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.PhaseShift

abstract class Use(val electricitySupply: ElectricitySupply){
    abstract val usage: Usage
    abstract val phaseShift: PhaseShift
    abstract fun maxVoltageDropAcceptablePercentage(): Float
}