package com.cjanie.voltagedropcalculator.businesslogic.models.line

import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing
import com.cjanie.voltagedropcalculator.businesslogic.factories.InstallationFactory
import com.cjanie.voltagedropcalculator.businesslogic.models.Installation
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.PhaseShift
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Resistance
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop
import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Conductor
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension

abstract class Line (
    val phaseShift: PhaseShift,
    conductor: Conductor,
    section: Section,
    private val intensity: Intensity,
    private val length: Length
) {

    protected abstract val phasing: Phasing

    private val resistance = Resistance(
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

    open fun maxLengthAcceptable(nominal_U: Tension): Length {
        //val delta_U_inVolt = functionalContext.maxVoltageDropPercentageAcceptable * nominal_U.inVolt / 100
        // return Length(inKilometer = delta_U_inVolt / K * I.inAmpere)
        TODO()
    }

}