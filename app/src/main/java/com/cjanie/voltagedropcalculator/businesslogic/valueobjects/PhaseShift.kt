package com.cjanie.voltagedropcalculator.businesslogic.valueobjects

import kotlin.math.pow
import kotlin.math.sqrt

data class PhaseShift(val cosPHI: Float) {

    companion object {
        fun sinPhiFrom(cosPhi: Float): Float {
            return sqrt(1 - cosPhi.pow(2))
        }
    }

    val sinPhi = sqrt(1 - cosPHI.pow(2))
}
