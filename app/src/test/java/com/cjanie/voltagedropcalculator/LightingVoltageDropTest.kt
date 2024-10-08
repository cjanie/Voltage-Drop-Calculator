package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.Conductor
import com.cjanie.voltagedropcalculator.businesslogic.FunctionalContext
import com.cjanie.voltagedropcalculator.businesslogic.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.Length
import com.cjanie.voltagedropcalculator.businesslogic.Line
import com.cjanie.voltagedropcalculator.businesslogic.LineSinglePhase
import com.cjanie.voltagedropcalculator.businesslogic.LineThreePhase
import com.cjanie.voltagedropcalculator.businesslogic.Material
import com.cjanie.voltagedropcalculator.businesslogic.Section
import com.cjanie.voltagedropcalculator.businesslogic.Tension
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LightingVoltageDropTest {
    // Contexte éclairage
    // Ligne triphasée
    // en cuivre
    // 70 mm2
    // 50 m
    // Parcourue par un courant d'une intensité de 150 Ampères

    val lineThreePhase = LineThreePhase(
        functionalContext = FunctionalContext.LIGHTING,
        conductor = Conductor(Material.COPPER),
        S = Section(inMillimeterSquare =  70f),
        I = Intensity(inAmpere =  150f),
        L = Length(inKilometer = 0.05f)
        //U = 230f
    )

    // Alimente 3 circuits monophasés
    // en cuivre
    // 2.5 mm2
    // 20 m
    // Parcourus par un courant d'une intensité de 20 Ampères

    val singlePhaseLines = Array<Line>(3) {
        LineSinglePhase(
            functionalContext = FunctionalContext.LIGHTING,
            conductor = Conductor(Material.COPPER),
            S = Section(inMillimeterSquare = 2.5f),
            I = Intensity(inAmpere = 20f),
            L = Length(inKilometer = 0.02f)
            //U = 230f
        )
    }

    @Test
    fun voltageDropCalculationForTheThreePhaseLine() {
        val DELTA_U = lineThreePhase.voltageDrop()
        assertEquals(2.5392857.toFloat(), DELTA_U.inVolt)
    }

    @Test
    fun voltageDropCalculationForTheSingleLines() {
        val delta_U = singlePhaseLines[0].voltageDrop()
        assertEquals(7.584f, delta_U.inVolt)
    }

    @Test
    fun percentageOnCompleteInstallation() {
        val cable = lineThreePhase
        val circuit = singlePhaseLines[0]
        val nominal_U = Tension(inVolt = 230f)
        val voltageDropInPercentage = cable.voltageDrop().percentage(nominal_U = nominal_U) + circuit.voltageDrop().percentage(nominal_U)

        assertEquals(4.4014287f, voltageDropInPercentage)
        //assertTrue(Line.isVoltageDropAcceptable(voltageDropInPercentage, FunctionalContext.LIGHTING))
    }

}