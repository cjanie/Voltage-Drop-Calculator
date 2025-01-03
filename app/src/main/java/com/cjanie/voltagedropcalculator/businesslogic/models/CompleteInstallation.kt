package com.cjanie.voltagedropcalculator.businesslogic.models

import com.cjanie.voltagedropcalculator.businesslogic.PhaseShiftInconsistancyException
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Use
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.MaxVoltageDropLimit
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.VoltageDrop

class CompleteInstallation(
    val use: Use,
    val input: Line,
    val output: Array<Line>,
    val nominalVoltage: Voltage
): Installation() {

    init {
        if(!isPhaseShiftConsistent(input, use.usage, use.electricitySupply))
            throw PhaseShiftInconsistancyException()
        if(!output.isEmpty()) {
            for (circuit in output) {
                if (!isPhaseShiftConsistent(circuit, usage = use.usage, use.electricitySupply))
                    throw PhaseShiftInconsistancyException()
            }
        }
    }

    // Voltage Drop calculations
    override val maxVoltageDropLimitPercentage = MaxVoltageDropLimit(use).percentage

    private val voltageDrop = calculateVoltageDrop()

    override val voltageDropInVolt = voltageDrop.inVolt
    override val voltageDropPercentage = voltageDrop.percentage(nominalVoltage)
    override val isVoltageDropAcceptable = maxVoltageDropLimitPercentage > voltageDrop.percentage(nominalVoltage)

    private fun calculateVoltageDrop(): VoltageDrop {
        val cableVoltageDrop = input.voltageDrop
        val circuitsVoltageDrop = if (output.isEmpty()) VoltageDrop(0f) else output[0].voltageDrop
        return VoltageDrop(inVolt = cableVoltageDrop.inVolt + circuitsVoltageDrop.inVolt)
    }

}