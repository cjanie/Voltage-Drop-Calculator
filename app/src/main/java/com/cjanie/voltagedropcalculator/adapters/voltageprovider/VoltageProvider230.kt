package com.cjanie.voltagedropcalculator.adapters.voltageprovider

import com.cjanie.voltagedropcalculator.businesslogic.gateways.VoltageProvider
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage

class VoltageProvider230: VoltageProvider {
    override fun provides(): Voltage {
        return Voltage(230f)
    }
}