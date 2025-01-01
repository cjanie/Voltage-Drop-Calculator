package com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects

import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage

class VoltageDrop(val inVolt: Float) {

    companion object {
        val unit = Voltage.unit
    }

    fun percentage(nominal_U: Voltage): Float {
        return 100 * this.inVolt / nominal_U.inVolt
    }

}
