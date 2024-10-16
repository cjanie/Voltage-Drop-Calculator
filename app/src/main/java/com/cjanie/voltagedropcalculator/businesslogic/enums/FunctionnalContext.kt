package com.cjanie.voltagedropcalculator.businesslogic.enums

enum class FunctionnalContext(
    val maxVoltageDropPercentageAcceptable: Float
) {
    LIGHTING(
        maxVoltageDropPercentageAcceptable = 6f
    )
}