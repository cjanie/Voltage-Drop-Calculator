package com.cjanie.voltagedropcalculator.adapters.device

import com.cjanie.voltagedropcalculator.businesslogic.gateways.Device
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity

class Light(private val consumes: Intensity): Device {
    override fun consumes(): Intensity {
        return consumes
    }
}