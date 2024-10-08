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
import com.cjanie.voltagedropcalculator.businesslogic.VoltageDrop
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

    val functionalContext = FunctionalContext.LIGHTING
    val lineThreePhase = LineThreePhase(
        functionalContext = functionalContext,
        conductor = Conductor(Material.COPPER),
        S = Section(inMillimeterSquare =  70f),
        I = Intensity(inAmpere =  150f),
        nominal_U = Tension(inVolt = 230f),
        L = Length(inKilometer = 0.05f)
    )

    // Alimente 3 circuits monophasés
    // en cuivre
    // 2.5 mm2
    // 20 m
    // Parcourus par un courant d'une intensité de 20 Ampères

    val singlePhaseLines = Array<Line>(3) {
        LineSinglePhase(
            functionalContext = functionalContext,
            conductor = Conductor(Material.COPPER),
            S = Section(inMillimeterSquare = 2.5f),
            I = Intensity(inAmpere = 20f),
            nominal_U = Tension(inVolt = 230f),
            L = Length(inKilometer = 0.02f)
        )
    }

    @Test
    fun voltageDropCalculationForTheThreePhaseLine() {
        val DELTA_U = lineThreePhase.voltageDropInVolt()
        assertEquals(2.5392857.toFloat(), DELTA_U)
    }

    @Test
    fun voltageDropCalculationForTheSingleLines() {
        val delta_U = singlePhaseLines[0].voltageDropInVolt()
        assertEquals(7.584f, delta_U)
    }

    @Test
    fun percentageOnCompleteInstallation() {
        val cable = lineThreePhase
        val circuit = singlePhaseLines[0]
        val voltageDropInPercentage = cable.voltageDropPercentage() + circuit.voltageDropPercentage()
        assertEquals(4.4014287f, voltageDropInPercentage)
        assertTrue(VoltageDrop.isAcceptable(voltageDropInPercentage, functionalContext))
    }

}