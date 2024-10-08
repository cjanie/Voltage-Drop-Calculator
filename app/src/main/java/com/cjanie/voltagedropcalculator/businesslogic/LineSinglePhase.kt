package com.cjanie.voltagedropcalculator.businesslogic

class LineSinglePhase(
    functionalContext: FunctionalContext,
    conductor: Conductor, S: Section, I: Intensity,
    nominal_U: Tension,
    L: Length

) : Line(functionalContext, conductor, S, I, nominal_U,L) {

    companion object {
        private val NUMBER_OF_PHASES = 2 // 2 phases or 1 phase and neutral
        private val PHASE_SHIFT_RATIO = NUMBER_OF_PHASES.toFloat()
    }

    override fun phaseShiftRatio(): Float {
        return PHASE_SHIFT_RATIO
    }

}
