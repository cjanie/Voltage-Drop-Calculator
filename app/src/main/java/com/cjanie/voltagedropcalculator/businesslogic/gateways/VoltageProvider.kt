package com.cjanie.voltagedropcalculator.businesslogic.gateways

import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage

interface VoltageProvider {
    fun provides(): Voltage
}