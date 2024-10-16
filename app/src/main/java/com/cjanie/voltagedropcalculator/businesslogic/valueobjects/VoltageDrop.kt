package com.cjanie.voltagedropcalculator.businesslogic.valueobjects

class VoltageDrop(val inVolt: Float) {

    fun percentage(nominal_U: Tension): Float {
        return 100 * this.inVolt / nominal_U.inVolt
    }

}
