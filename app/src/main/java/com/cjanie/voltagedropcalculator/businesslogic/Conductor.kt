package com.cjanie.voltagedropcalculator.businesslogic

class Conductor(private val material: Material) {

    companion object {
        private val REACTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH = 0.08f
    }

    fun RESISTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH(section: Section): Float {
        return this.material.RESISTANCE_IN_OHM_PER_MILLIMETER_SQUARE_SECTION_AND_KILOMETER_LENGTH / section.inMillimeterSquare
    }

    fun REACTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH(): Float {
        return REACTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH
    }
}