package com.cjanie.voltagedropcalculator.businesslogic.models

interface CalculateVoltageDrop {
    val maxVoltageDropLimitPercentage: Float
    val voltageDropInVolt: Float
    val voltageDropPercentage: Float
    val isVoltageDropAcceptable: Boolean
}