package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.VoltageDropCalculator
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section
import org.junit.Assert.assertEquals
import org.junit.Test


class VoltageDropCalculatorTest {

    @Test
    fun voltageDropAtEndOfLineLighting() {
        // circuit 20 m
        // copper 2.5 mm2
        // I = 20 A
        val S = Section(inMillimeterSquare = 2.5f)
        val I = Intensity(inAmpere = 20f)
        val L = Length(inKilometer = 0.02f)

        val delta_U = VoltageDropCalculator(
            conductorMaterial = ConductorMaterial.COPPER,
            S = S,
            usage = Usage.LIGHTING,
            I = I,
            L = L
        ).voltageDropAtEndOfLine

        assertEquals(7.584f, delta_U.inVolt)
    }
}