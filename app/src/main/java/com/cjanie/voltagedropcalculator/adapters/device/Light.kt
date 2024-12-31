package com.cjanie.voltagedropcalculator.adapters.device

import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.gateways.Device
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity

class Light(private val consumes: Intensity): Device {
    override fun consumes(): Intensity {
        return consumes
    }

    override fun usage(): Usage {
        return Usage.LIGHTING
    }
}