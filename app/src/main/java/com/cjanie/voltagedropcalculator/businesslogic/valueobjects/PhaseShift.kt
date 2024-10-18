package com.cjanie.voltagedropcalculator.businesslogic.valueobjects

import kotlin.math.pow
import kotlin.math.sqrt

data class PhaseShift(val cosPHI: Float) {
    val sinPhi = sqrt(1 - cosPHI.pow(2))
}
