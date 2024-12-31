package com.cjanie.voltagedropcalculator.businesslogic.valueobjects

import com.cjanie.voltagedropcalculator.businesslogic.enums.OhmLawParameterUnit

class Voltage(val inVolt: Float) {
    companion object {
        val unit = OhmLawParameterUnit.VOLT
    }
}