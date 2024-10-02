package com.cjanie.voltagedropcalculator.businesslogic

import kotlin.math.sqrt

class LineThreePhase(
    conductor: Conductor,
    current: Current,
    functionalContext: FunctionalContext,
    lengthInKilometer: Float,
    tensionNominalInVolt: Float
) : Line(conductor, current, functionalContext, lengthInKilometer, tensionNominalInVolt) {

    companion object {
        private val NUMBER_OF_PHASES = 3
        private val PHASE_SHIFT_RATIO = sqrt(NUMBER_OF_PHASES.toFloat())
    }

    override fun phaseShiftRatio(): Float {
        return PHASE_SHIFT_RATIO
    }

    override fun calculateVoltageDropInVolt(): Float {
        // Between Phases and neutral
        return super.calculateVoltageDropInVolt() / PHASE_SHIFT_RATIO
    }

}