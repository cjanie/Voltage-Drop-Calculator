package com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects

import com.cjanie.voltagedropcalculator.businesslogic.enums.DimensionUnit

class Section(val inMillimeterSquare: Float) {
    companion object {
        val unit = DimensionUnit.MILLIMETER_SQUARE
    }
}