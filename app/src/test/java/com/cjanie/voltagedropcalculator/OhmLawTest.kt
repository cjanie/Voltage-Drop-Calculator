package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.adapters.device.Light
import com.cjanie.voltagedropcalculator.adapters.voltageprovider.VoltageProvider230
import com.cjanie.voltagedropcalculator.businesslogic.gateways.Device
import com.cjanie.voltagedropcalculator.businesslogic.ohmlaw.OhmLaw
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Resistance
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage
import org.junit.Assert.assertEquals
import org.junit.Test


class OhmLawTest {

    @Test
    fun resistance() {
        // Ohm Law: U=RI
        // so R = U/I

        // Voltage Provider
        val voltageProvider = VoltageProvider230()
        // Consumer
        val device = Light(consumes = Intensity(inAmpere = 20f))

        val U: Voltage = voltageProvider.provides()
        val I: Intensity = device.consumes()
        val R = Resistance(inOhm = U.inVolt / I.inAmpere)

        assertEquals(230f / 20f, R.inOhm)
        assertEquals(R.inOhm, OhmLaw.resistance(U, I).inOhm)

        assertEquals(230f, OhmLaw.voltage(R, I).inVolt)
        assertEquals(20f, OhmLaw.intensity(U, R).inAmpere)
    }

}