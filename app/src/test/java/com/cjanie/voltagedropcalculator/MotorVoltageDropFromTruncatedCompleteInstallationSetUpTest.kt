package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.PhaseShiftInconsistancyException
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Copper
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineSinglePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.line.LineThreePhase
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Motor
import com.cjanie.voltagedropcalculator.businesslogic.usecases.TruncatedInstallationSetUpUseCase
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.PhaseShift
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop
import junit.framework.TestCase.assertEquals

import org.junit.Test


class MotorVoltageDropFromTruncatedCompleteInstallationSetUpTest {

    @Test(expected = NullValueException::class)
    fun truncatedInstallationSetUpNotSucceed() {
        val truncatedInstallationSetUpUseCase = TruncatedInstallationSetUpUseCase(
            usage = Usage.MOTOR,
            electricitySupply = ElectricitySupply.PRIVATE,
            nominalVoltage = Voltage(inVolt = 400f),
            inputCableVoltageDrop = VoltageDrop(10f)
        )
        truncatedInstallationSetUpUseCase.getInstallation()
    }

    @Test
    fun truncatedInstallationSetUpSucceed() {

        val truncatedInstallationSetUpUseCase = TruncatedInstallationSetUpUseCase(
            usage = Usage.MOTOR,
            electricitySupply = ElectricitySupply.PRIVATE,
            nominalVoltage = Voltage(inVolt = 400f),
            inputCableVoltageDrop = VoltageDrop(10f)
        )

        val use = Motor(ElectricitySupply.PRIVATE)
        val outputCircuit = LineSinglePhase(
            phaseShift = use.phaseShift,
            conductor = Copper(),
            section = Section(50f),
            intensity = Intensity(100f),
            length = Length(0.05f)
        )
        truncatedInstallationSetUpUseCase.addOutputCircuit(outputCircuit)

        val installation = truncatedInstallationSetUpUseCase.getInstallation()
        //assertEquals(0.64f, 0.8.pow(2))
        assertEquals(0.6f, PhaseShift(0.8f).sinPhi)
        assertEquals(1, installation.outputCircuits.size)

        assertEquals(1f, installation.outputCircuits[0].resistance.inOhmPerKilometer)

        assertEquals(5f, outputCircuit.voltageDrop.inVolt)

        assertEquals(15f, installation.voltageDropInVolt)

    }

    @Test(expected = PhaseShiftInconsistancyException::class)
    fun phaseShiftInconsistency() {
        val truncatedInstallationSetUpUseCase = TruncatedInstallationSetUpUseCase(
            usage = Usage.MOTOR,
            electricitySupply = ElectricitySupply.PRIVATE,
            nominalVoltage = Voltage(inVolt = 400f),
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

