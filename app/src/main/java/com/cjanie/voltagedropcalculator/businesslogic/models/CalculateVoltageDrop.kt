package com.cjanie.voltagedropcalculator.businesslogic.models

import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.MaxVoltageDropLimit

interface CalculateVoltageDrop {
    val maxVoltageDropLimitPercentage: Float
    val voltageDropInVolt: Float
    val voltageDropPercentage: Float
    val isVoltageDropAcceptable: Boolean
}