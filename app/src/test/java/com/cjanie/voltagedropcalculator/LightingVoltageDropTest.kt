package com.cjanie.voltagedropcalculator

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

abstract class Conductor(
    private val sectionInMillimeterSquare: Float,
    val lengthInKilometer: Float
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
class Copper(sectionInMillimeterSquare: Float, lengthInKilometer: Float) :
    Conductor(sectionInMillimeterSquare, lengthInKilometer) {

    companion object {
        private val RESISTANCE_IN_OHM_PER_MILLIMETER_SQUARE_SECTION_AND_KILOMETER_LENGTH = 23.7f
    }

    override fun RESISTANCE_IN_OHM_PER_MILLIMETER_SQUARE_SECTION_AND_KILOMETER_LENGTH(): Float {
        return RESISTANCE_IN_OHM_PER_MILLIMETER_SQUARE_SECTION_AND_KILOMETER_LENGTH
    }

}

class Aluminium(sectionInMillimeterSquare: Float,
                lengthInKilometer: Float
) : Conductor(sectionInMillimeterSquare, lengthInKilometer) {

    companion object {
        private val RESISTANCE_IN_OHM_PER_MILLIMETER_SQUARE_SECTION_AND_KILOMETER_LENGTH = 37.6f
    }

    override fun RESISTANCE_IN_OHM_PER_MILLIMETER_SQUARE_SECTION_AND_KILOMETER_LENGTH(): Float {
        return RESISTANCE_IN_OHM_PER_MILLIMETER_SQUARE_SECTION_AND_KILOMETER_LENGTH
    }

}

class Current(val intensityInAmpere: Float)

abstract class Line(
    private val conductor: Conductor,
    private val current: Current,
    private val functionalContext: FunctionalContext
) {

    private val I = this.current.intensityInAmpere

    private val L = this.conductor.lengthInKilometer
    private val R = this.conductor.RESISTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH()
    private val X = this.conductor.REACTANCE_LINEAR_IN_OHM_PER_KILOMETER_LENGTH()

    private val phaseShift: PhaseShift by lazy {
        when(this.functionalContext) {
            FunctionalContext.LIGHTING -> PhaseShiftLighting()
        }
    }
    private val COS_PHI = this.phaseShift.COS_PHI()
    private val SIN_PHI = this.phaseShift.SIN_PHI()

    protected val R_COS_PHI_PLUS_X_SIN_PHI = R * COS_PHI + X * SIN_PHI
    private val K = this.K()



    abstract fun calculateVoltageDropInVolt(): Float

    protected abstract fun K(): Float

    protected fun voltageDropInVoltBetweenPhases(): Float {
        return K * I * L
    }

    fun supplies(lines: Array<Line>) {
        // TODO
    }
}

abstract class PhaseShift {
    abstract fun COS_PHI(): Float
    abstract fun SIN_PHI(): Float
}

class PhaseShiftLighting: PhaseShift() {
    companion object {
        private val PHI = 0f
    }

    override fun COS_PHI(): Float {
        return cos(PHI)
    }

    override fun SIN_PHI(): Float {
        return sin(PHI)
    }
}

enum class FunctionalContext {
    LIGHTING
}

class LineThreePhase(
    conductor: Conductor,
    current: Current,
    functionnalContext: FunctionalContext
) : Line(conductor, current, functionnalContext) {

    companion object {
        private val NUMBER_OF_PHASES = 3
    }

    override fun calculateVoltageDropInVolt(): Float {
        return this.voltageDropInVoltBetweenPhasesAndNeutral()
    }

    override fun K(): Float {
        return sqrt(NUMBER_OF_PHASES.toFloat()) * R_COS_PHI_PLUS_X_SIN_PHI
    }

    private fun voltageDropInVoltBetweenPhasesAndNeutral(): Float {
        return this.voltageDropInVoltBetweenPhases() / sqrt(NUMBER_OF_PHASES.toFloat())
    }

}

class LineSinglePhase(
    conductor: Conductor,
    current: Current,
    functionnalContext: FunctionalContext,
) : Line(conductor, current, functionnalContext) {

    companion object {
        private val NUMBER_OF_PHASES = 2 // 2 phases or 1 phase and neutral
    }

    override fun calculateVoltageDropInVolt(): Float {
        return this.voltageDropInVoltBetweenPhases()
    }

    override fun K(): Float {
        return NUMBER_OF_PHASES * R_COS_PHI_PLUS_X_SIN_PHI
    }
}

class LightingVoltageDropTest {
    // Contexte éclairage
    // Ligne triphasée
    // en cuivre
    // 70 mm2
    // 50 m
    // Parcourue par un courant d'une intensité de 150 Ampères

    val lineThreePhase = LineThreePhase(
        conductor = Copper(
            sectionInMillimeterSquare = 70f,
            lengthInKilometer = 0.050f),
        current = Current(intensityInAmpere =  150f),
        functionnalContext = FunctionalContext.LIGHTING
    )

    // Alimente 3 circuits monophasés
    // en cuivre
    // 2.5 mm2
    // 20 m
    // Parcourus par un courant d'une intensité de 20 Ampères

    val singlePhaseLines = Array<Line>(3) {
        LineSinglePhase(
            conductor = Copper(
                sectionInMillimeterSquare = 2.5f,
                lengthInKilometer = 0.020f),
            current = Current(intensityInAmpere = 20f),
            functionnalContext = FunctionalContext.LIGHTING
        )
    }

    fun supply() {
        this.lineThreePhase.supplies(this.singlePhaseLines)
    }
    @Test
    fun voltageDropCalculationForTheThreePhaseLine() {
        val DELTA_U = lineThreePhase.calculateVoltageDropInVolt()
        assertEquals(2.5392857.toFloat(), DELTA_U)
    }

    @Test
    fun voltageDropCalculationForTheSingleLines() {
        val delta_U = singlePhaseLines[0].calculateVoltageDropInVolt()
        assertEquals(7.584f, delta_U)
    }

}