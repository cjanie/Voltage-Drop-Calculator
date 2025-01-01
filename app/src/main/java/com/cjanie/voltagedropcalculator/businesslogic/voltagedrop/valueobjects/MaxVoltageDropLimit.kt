package com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects

import com.cjanie.voltagedropcalculator.businesslogic.models.use.Use


class MaxVoltageDropLimit(use: Use) {
    val percentage = use.maxVoltageDropAcceptablePercentage()
}
