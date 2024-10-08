package com.cjanie.voltagedropcalculator.businesslogic

enum class FunctionalContext(
    val phi: Float,
    val maxVoltageDropPercentageAcceptable: Float
) {
    LIGHTING(
        phi = 0f,
        maxVoltageDropPercentageAcceptable = 6f
    )
}