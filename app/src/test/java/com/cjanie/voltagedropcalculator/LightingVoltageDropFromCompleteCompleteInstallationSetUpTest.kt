package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.factories.ConductorFactory
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.models.CompleteInstallation
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineSinglePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineThreePhase
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.usecases.InstallationSetUpUseCase
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LightingVoltageDropFromCompleteCompleteInstallationSetUpTest {

    @Test
    fun installationPrivateLighting() {
        // Contexte éclairage
        // Ligne triphasée
        // en cuivre
        // 70 mm2
        // 50 m
        // Parcourue par un courant d'une intensité de 150 Ampères
        val use = Lighting(ElectricitySupply.PRIVATE)

        val installationSetUp = InstallationSetUpUseCase(use, Tension(inVolt = 230f))
        installationSetUp.addInput(LineThreePhase(
                phaseShift = use.phaseShift,
                conductor = ConductorFactory.conductor(ConductorMaterial.COPPER),
                section = Section(inMillimeterSquare =  70f),
                length = Length(inKilometer = 0.05f),
                intensity = Intensity(inAmpere =  150f)
            )
        )

        // Alimente 3 circuits monophasés
        // en cuivre
        // 2.5 mm2
        // 20 m
        // Parcourus par un courant d'une intensité de 20 Ampères

        installationSetUp.addOutput(Array(3) {
            LineSinglePhase(
                phaseShift = use.phaseShift,
                conductor = ConductorFactory.conductor(ConductorMaterial.COPPER),
                section = Section(inMillimeterSquare = 2.5f),
                length = Length(inKilometer = 0.02f),
                intensity = Intensity(inAmpere = 20f),
            )
        })

        val installation = installationSetUp.getInstallation()

        assertEquals(4.4014287f, installation?.voltageDropPercentage)
        assertTrue(installation!!.isVoltageDropAcceptable)
    }

    @Test
    fun voltageDropCalculationForTheThreePhaseLine() {
        val use = Lighting(ElectricitySupply.PRIVATE)
        val lineThreePhase = LineThreePhase(
            phaseShift = use.phaseShift,
            conductor = ConductorFactory.conductor(ConductorMaterial.COPPER),
            section = Section(inMillimeterSquare =  70f),
            length = Length(inKilometer = 0.05f),
            intensity = Intensity(inAmpere =  150f)
        )
        val completeInstallation = CompleteInstallation(
            use = use,
            input = lineThreePhase,
            output = lineThreePhase.output,
            nominalTension = Tension(inVolt = 230f)
        )
        val DELTA_U = completeInstallation.voltageDropInVolt
        assertEquals(2.5392857.toFloat(), DELTA_U)
    }

    @Test
    fun voltageDropCalculationForTheSingleLines() {
        val use = Lighting(ElectricitySupply.PRIVATE)
        val cable = LineSinglePhase(
            phaseShift = use.phaseShift,
            conductor = ConductorFactory.conductor(ConductorMaterial.COPPER),
            section = Section(inMillimeterSquare = 2.5f),
            length = Length(inKilometer = 0.02f),
            intensity = Intensity(inAmpere = 20f),
        )

        val completeInstallation = CompleteInstallation(
            use = use,
            input = cable,
            output = cable.output,
            nominalTension = Tension(inVolt = 230f)
        )
        val delta_U = completeInstallation.voltageDropInVolt
        assertEquals(7.584f, delta_U)
    }

}