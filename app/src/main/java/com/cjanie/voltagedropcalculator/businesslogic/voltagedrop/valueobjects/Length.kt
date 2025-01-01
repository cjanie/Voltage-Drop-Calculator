package com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects

import com.cjanie.voltagedropcalculator.businesslogic.enums.DimensionUnit

class Length(val inKilometer: Float) {
    companion object {
        val unit = DimensionUnit.KILOMETER
    }
}