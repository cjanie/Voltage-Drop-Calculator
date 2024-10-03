package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.Copper
import com.cjanie.voltagedropcalculator.businesslogic.Current
import com.cjanie.voltagedropcalculator.businesslogic.FunctionalContext
import com.cjanie.voltagedropcalculator.businesslogic.Line
import com.cjanie.voltagedropcalculator.businesslogic.LineSinglePhase
import com.cjanie.voltagedropcalculator.businesslogic.LineThreePhase
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
        conductor = Copper(sectionInMillimeterSquare = 70f),
        current = Current(intensityInAmpere =  150f),
        functionalContext = FunctionalContext.LIGHTING,
        tensionNominalInVolt = 230f
    )

    // Alimente 3 circuits monophasés
    // en cuivre
    // 2.5 mm2
    // 20 m
    // Parcourus par un courant d'une intensité de 20 Ampères

    val singlePhaseLines = Array<Line>(3) {
        LineSinglePhase(
            conductor = Copper(sectionInMillimeterSquare = 2.5f),
            current = Current(intensityInAmpere = 20f),
            functionalContext = FunctionalContext.LIGHTING,
            tensionNominalInVolt = 230f
        )
    }

    @Test
    fun voltageDropCalculationForTheThreePhaseLine() {
        val DELTA_U = lineThreePhase.voltageDropInVolt(lengthInKilometer = 0.050f)
        assertEquals(2.5392857.toFloat(), DELTA_U)
    }

    @Test
    fun voltageDropCalculationForTheSingleLines() {
        val delta_U = singlePhaseLines[0].voltageDropInVolt(lengthInKilometer = 0.020f)
        assertEquals(7.584f, delta_U)
    }

    @Test
    fun percentageOnCompleteInstallation() {
        val cable = lineThreePhase
        val circuit = singlePhaseLines[0]
        val voltageDropInPercentage = cable.voltageDropInPercentage(lengthInKilometer = 0.050f) + circuit.voltageDropInPercentage(lengthInKilometer = 0.020f)

        assertEquals(4.4014287f, voltageDropInPercentage)
        assertTrue(Line.isVoltageDropAcceptable(voltageDropInPercentage, FunctionalContext.LIGHTING))
    }

}