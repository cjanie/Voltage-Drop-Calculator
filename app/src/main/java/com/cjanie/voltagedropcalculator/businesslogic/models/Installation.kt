package com.cjanie.voltagedropcalculator.businesslogic.models

import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Use
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.MaxVoltageDropLimit
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop

class Installation(
    val use: Use,
    val input: Line,
    val output: Array<Line>,
    val nominalTension: Tension
): CalculateVoltageDrop {

    // Voltage Drop calculations
    override val maxVoltageDropLimitPercentage = MaxVoltageDropLimit(use).percentage

    private val voltageDrop = calculateVoltageDrop()

    override val voltageDropInVolt = voltageDrop.inVolt
    override val voltageDropPercentage = voltageDrop.percentage(nominalTension)
    override val isVoltageDropAcceptable = maxVoltageDropLimitPercentage > voltageDrop.percentage(nominalTension)

    private fun calculateVoltageDrop(): VoltageDrop {
        val cableVoltageDrop = input.voltageDrop
        val circuitsVoltageDrop = if (output.isEmpty()) VoltageDrop(0f) else output[0].voltageDrop
        return VoltageDrop(inVolt = cableVoltageDrop.inVolt + circuitsVoltageDrop.inVolt)
    }

}