package com.cjanie.voltagedropcalculator.businesslogic.valueobjects

import com.cjanie.voltagedropcalculator.businesslogic.enums.OhmLawParameterUnit

class Resistance(val inOhm: Float) {
    companion object {
        val unit = OhmLawParameterUnit.OHM
    }
}