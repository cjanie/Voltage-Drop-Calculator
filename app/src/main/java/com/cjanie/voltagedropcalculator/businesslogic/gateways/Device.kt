package com.cjanie.voltagedropcalculator.businesslogic.gateways

import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity

interface Device {
    fun consumes(): Intensity
    fun usage(): Usage
}