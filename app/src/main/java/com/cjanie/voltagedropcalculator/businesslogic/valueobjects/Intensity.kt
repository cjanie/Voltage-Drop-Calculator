package com.cjanie.voltagedropcalculator.businesslogic.valueobjects

import com.cjanie.voltagedropcalculator.businesslogic.enums.OhmLawParameterUnit

class Intensity(val inAmpere: Float) {
    companion object {
        val unit = OhmLawParameterUnit.AMPERE
    }
}