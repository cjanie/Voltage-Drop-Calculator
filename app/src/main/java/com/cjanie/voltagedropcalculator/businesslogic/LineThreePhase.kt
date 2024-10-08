package com.cjanie.voltagedropcalculator.businesslogic

import kotlin.math.sqrt

class LineThreePhase(
    functionalContext: FunctionalContext, conductor: Conductor, S: Section,
    I: Intensity,
    nominal_U: Tension,
    L: Length
) : Line(functionalContext, conductor, S, I, nominal_U, L) {

    companion object {
        private val NUMBER_OF_PHASES = 3
        private val PHASE_SHIFT_RATIO = sqrt(NUMBER_OF_PHASES.toFloat())
    }

    override fun phaseShiftRatio(): Float {
        return PHASE_SHIFT_RATIO
    }

    override fun voltageDrop(): VoltageDrop {
        // Between Phases and neutral
        return VoltageDrop(
            inVolt = super.voltageDrop().inVolt / PHASE_SHIFT_RATIO
        )
    }

    override fun maxLengthAcceptable(): Length {
        return Length(inKilometer = super.maxLengthAcceptable().inKilometer * PHASE_SHIFT_RATIO)
    }

}