package com.cjanie.voltagedropcalculator.businesslogic.models.line

import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.PhaseShift
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop
import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Conductor
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section

class LineThreePhase(
    phaseShift: PhaseShift,
    conductor: Conductor,
    section: Section,
    intensity: Intensity,
    length: Length
) : Line(phaseShift, conductor, section, intensity, length) {

    override val phasing: Phasing
        get() = Phasing.THREE_PHASE

    override fun voltageDrop(): VoltageDrop {
        // Between Phases and neutral
        return VoltageDrop(
            inVolt = super.voltageDrop().inVolt // / phasing.ratio
        )
    }
/*
    override fun maxLengthAcceptable(): Length {
        //return Length(inKilometer = super.maxLengthAcceptable().inKilometer * PHASE_SHIFT_RATIO)
        TODO()
    }

 */

}