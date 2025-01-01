package com.cjanie.voltagedropcalculator.businesslogic.models.line

import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.PhaseShift
import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Resistance
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.VoltageDrop
import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Conductor
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage

abstract class Line (
    val phaseShift: PhaseShift,
    val conductor: Conductor,
    val section: Section,
    val intensity: Intensity,
    val length: Length
) {

    abstract val phasing: Phasing

    val resistance = Resistance(
        conductor = conductor,
        section = section,
        phaseShift = phaseShift,
        phasingRatio = phasing.ratio
    )

    val voltageDrop = voltageDrop()

    protected open fun voltageDrop(): VoltageDrop {
        return VoltageDrop( inVolt = resistance.inOhmPerKilometer * intensity.inAmpere * length.inKilometer)
    }

    var output: Array<Line> = emptyArray()

    fun supplies(circuits: Array<Line>) {
        output = circuits
    }

    // Max length acceptable

    open fun maxLengthAcceptable(nominal_U: Voltage): Length {
        //val delta_U_inVolt = functionalContext.maxVoltageDropPercentageAcceptable * nominal_U.inVolt / 100
        // return Length(inKilometer = delta_U_inVolt / K * I.inAmpere)
        TODO()
    }

}