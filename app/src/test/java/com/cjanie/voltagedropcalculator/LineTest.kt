package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.adapters.device.Light
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.LineFactory
import org.junit.Assert.assertEquals
import org.junit.Test


class LineTest {

    @Test
    fun voltageDropAtEndOfLineLighting() {
        // circuit 20 m
        // copper 2.5 mm2
        // I = 20 A
        val S = Section(inMillimeterSquare = 2.5f)
        val I = Intensity(inAmpere = 20f)
        val L = Length(inKilometer = 0.02f)

        val line = LineFactory.line(
            length = L,
            section = S,
            device = Light(consumes = I)
        )

        assertEquals(7.584f, line.voltageDropAtEndOfLine().inVolt)
    }
}