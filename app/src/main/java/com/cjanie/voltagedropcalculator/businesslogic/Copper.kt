package com.cjanie.voltagedropcalculator.businesslogic

class Copper(sectionInMillimeterSquare: Float) :
    Conductor(sectionInMillimeterSquare) {

    companion object {
        private val RESISTANCE_IN_OHM_PER_MILLIMETER_SQUARE_SECTION_AND_KILOMETER_LENGTH = 23.7f
    }

    override fun RESISTANCE_IN_OHM_PER_MILLIMETER_SQUARE_SECTION_AND_KILOMETER_LENGTH(): Float {
        return RESISTANCE_IN_OHM_PER_MILLIMETER_SQUARE_SECTION_AND_KILOMETER_LENGTH
    }

}