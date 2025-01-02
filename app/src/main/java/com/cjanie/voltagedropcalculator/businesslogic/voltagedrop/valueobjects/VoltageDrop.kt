package com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects

import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage
import com.cjanie.voltagedropcalculator.ui.viewmodels.VoltageDropViewModel

class VoltageDrop(val inVolt: Float) {

    companion object {
        val unit = Voltage.unit

        fun voltageDropAtEndOfLine(K: ConductorVoltageDropProperty, I: Intensity, L: Length): VoltageDrop {
            return VoltageDrop(inVolt =
                K.inVoltPerAmpereAndPerKilometer * I.inAmpere * L.inKilometer
            )
        }

        fun maxPercentageAcceptable(usage: Usage): Float {
            return when (usage) {
                Usage.LIGHTING -> 4.4f
                Usage.MOTOR -> 6f
            }
        }
    }

    fun percentage(nominal_U: Voltage): Float {
        return 100 * this.inVolt / nominal_U.inVolt
    }

    fun isVoltageDropAcceptable(nominal_U: Voltage, usage: Usage): Boolean {
        return percentage(nominal_U) <= maxPercentageAcceptable(usage)
    }

}
