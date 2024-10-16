package com.cjanie.voltagedropcalculator.businesslogic.enums

import kotlin.math.sqrt

enum class Phasing(val ratio: Float) {
    SINGLE_PHASE(ratio = 2f),
    THREE_PHASE(ratio = sqrt(3f))
}