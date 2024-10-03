package com.cjanie.voltagedropcalculator.businesslogic

import kotlin.math.sqrt

class LineThreePhase(
    conductor: Conductor,
    current: Current,
    functionalContext: FunctionalContext,
    tensionNominalInVolt: Float
) : Line(conductor, current, functionalContext, tensionNominalInVolt) {

    companion object {
        private val NUMBER_OF_PHASES = 3
        private val PHASE_SHIFT_RATIO = sqrt(NUMBER_OF_PHASES.toFloat())
    }

    override fun phaseShiftRatio(): Float {
        return PHASE_SHIFT_RATIO
    }

    override fun voltageDropInVolt(lengthInKilometer: Float): Float {
        // Between Phases and neutral
        return super.voltageDropInVolt(lengthInKilometer) / PHASE_SHIFT_RATIO
    }

}