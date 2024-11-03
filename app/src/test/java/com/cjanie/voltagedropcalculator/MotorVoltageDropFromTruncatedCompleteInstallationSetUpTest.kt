package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.PhaseShiftInconsistancyException
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Copper
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineThreePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Motor
import com.cjanie.voltagedropcalculator.businesslogic.usecases.TruncatedInstallationSetUpUseCase
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop
import junit.framework.TestCase.assertEquals

import org.junit.Test

class MotorVoltageDropFromTruncatedCompleteInstallationSetUpTest {

    @Test(expected = NullValueException::class)
    fun truncatedInstallationSetUpNotSucceed() {
        val truncatedInstallationSetUpUseCase = TruncatedInstallationSetUpUseCase(
            usage = Usage.MOTOR,
            electricitySupply = ElectricitySupply.PRIVATE,
            nominalTension = Tension(inVolt = 400f),
            inputCableVoltageDrop = VoltageDrop(10f)
        )
        truncatedInstallationSetUpUseCase.getInstallation()
    }

    @Test
    fun truncatedInstallationSetUpSucceed() {

        val truncatedInstallationSetUpUseCase = TruncatedInstallationSetUpUseCase(
            usage = Usage.MOTOR,
            electricitySupply = ElectricitySupply.PRIVATE,
            nominalTension = Tension(inVolt = 400f),
            inputCableVoltageDrop = VoltageDrop(10f)
        )

        val use = Motor(ElectricitySupply.PRIVATE)
        truncatedInstallationSetUpUseCase.addOutputCircuit(
            LineThreePhase(
                phaseShift = use.phaseShift,
                conductor = Copper(),
                section = Section(35f),
                intensity = Intensity(100f),
                length = Length(0.05f)
            )
        )
        val installation = truncatedInstallationSetUpUseCase.getInstallation()
        assertEquals(1, installation.outputCircuits.size)

    }

    @Test(expected = PhaseShiftInconsistancyException::class)
    fun phaseShiftInconsistency() {
        val truncatedInstallationSetUpUseCase = TruncatedInstallationSetUpUseCase(
            usage = Usage.MOTOR,
            electricitySupply = ElectricitySupply.PRIVATE,
            nominalTension = Tension(inVolt = 400f),
            inputCableVoltageDrop = VoltageDrop(10f)
        )

        val use = Lighting(ElectricitySupply.PRIVATE)
        truncatedInstallationSetUpUseCase.addOutputCircuit(
            LineThreePhase(
                phaseShift = use.phaseShift,
                conductor = Copper(),
                section = Section(35f),
                intensity = Intensity(100f),
                length = Length(0.05f)
            )
        )
    }
}

