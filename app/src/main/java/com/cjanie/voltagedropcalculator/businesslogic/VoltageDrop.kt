package com.cjanie.voltagedropcalculator.businesslogic

class Intensity(val inAmpere: Float)

class Length(val inKilometer: Float)

class Tension(val inVolt: Float)

class VoltageDrop(val inVolt: Float) {

    fun percentage(nominal_U: Tension): Float {
        return 100 * this.inVolt / nominal_U.inVolt
    }

    fun isAcceptable(nominal_U: Tension, functionalContext: FunctionalContext): Boolean {
        return isAcceptable(this.percentage(nominal_U), functionalContext)
    }

    companion object {
        fun isAcceptable(voltageDropPercentage: Float, functionalContext: FunctionalContext): Boolean {
            return voltageDropPercentage < functionalContext.maxVoltageDropPercentageAcceptable
        }
    }

}
