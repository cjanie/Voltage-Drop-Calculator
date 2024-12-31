package com.cjanie.voltagedropcalculator.businesslogic.models

import com.cjanie.voltagedropcalculator.businesslogic.PhaseShiftInconsistancyException
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop

class TruncatedInstallation(
    val usage: Usage,
    val electricitySupply: ElectricitySupply,
    val nominalVoltage: Voltage,
    val inputCableVoltageDrop: VoltageDrop,
    val outputCircuits: Array<Line> = emptyArray()
) : Installation() {

    init {
        for (circuit in outputCircuits) {
            if (!isPhaseShiftConsistent(circuit, usage, electricitySupply))
                throw PhaseShiftInconsistancyException()
        }
    }

    override val maxVoltageDropLimitPercentage: Float
        get() = maxVoltageDropLimitPercentage
    override val voltageDropInVolt: Float
        get() = inputCableVoltageDrop.inVolt + outputCircuits[0].voltageDrop.inVolt
    override val voltageDropPercentage: Float
        get() = VoltageDrop(inVolt = voltageDropInVolt).percentage(nominalVoltage)
    override val isVoltageDropAcceptable: Boolean
        get() = voltageDropPercentage < maxVoltageDropLimitPercentage


}