package com.cjanie.voltagedropcalculator.businesslogic.models

import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Use
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.MaxVoltageDropLimit
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop

class Installation(
    use: Use,
    private val cable: Line,
    private val circuits: Array<Line> = emptyArray(),
    nominalTension: Tension
) {

    val maxVoltageDropLimitPercentage = MaxVoltageDropLimit(use).percentage

    private val voltageDrop = calculateVoltageDrop()

    val voltageDropInVolt = voltageDrop.inVolt
    val voltageDropPercentage = voltageDrop.percentage(nominalTension)
    val isVoltageDropAcceptable = maxVoltageDropLimitPercentage > voltageDrop.percentage(nominalTension)

    private fun calculateVoltageDrop(): VoltageDrop {
        val cableVoltageDrop = cable.voltageDrop
        val circuitsVoltageDrop = if (circuits.isEmpty()) VoltageDrop(0f) else circuits[0].voltageDrop
        return VoltageDrop(inVolt = cableVoltageDrop.inVolt + circuitsVoltageDrop.inVolt)
    }

}