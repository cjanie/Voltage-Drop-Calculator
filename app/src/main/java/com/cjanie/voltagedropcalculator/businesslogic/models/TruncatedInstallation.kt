package com.cjanie.voltagedropcalculator.businesslogic.models

import com.cjanie.voltagedropcalculator.businesslogic.PhaseShiftInconsistancyException
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop

class TruncatedInstallation(
    val usage: Usage,
    val electricitySupply: ElectricitySupply,
    val nominalTension: Tension,
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
        get() = TODO("Not yet implemented")
    override val voltageDropInVolt: Float
        get() = TODO("Not yet implemented")
    override val voltageDropPercentage: Float
        get() = TODO("Not yet implemented")
    override val isVoltageDropAcceptable: Boolean
        get() = TODO("Not yet implemented")


}