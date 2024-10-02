package com.cjanie.voltagedropcalculator.businesslogic

abstract class Conductor(
    private val sectionInMillimeterSquare: Float
) {

    companion object {
        private val REACTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH = 0.08f
    }

    abstract protected fun RESISTANCE_IN_OHM_PER_MILLIMETER_SQUARE_SECTION_AND_KILOMETER_LENGTH(): Float

    fun RESISTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH(): Float {
        return this.RESISTANCE_IN_OHM_PER_MILLIMETER_SQUARE_SECTION_AND_KILOMETER_LENGTH() / sectionInMillimeterSquare
    }

    fun REACTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH(): Float {
        return REACTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH
    }
}