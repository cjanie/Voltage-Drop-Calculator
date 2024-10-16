package com.cjanie.voltagedropcalculator.businesslogic.models.use

import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.PhaseShift

abstract class Use {
    abstract val phaseShift: PhaseShift
    abstract fun maxVoltageDropAcceptablePercentage(): Float
}