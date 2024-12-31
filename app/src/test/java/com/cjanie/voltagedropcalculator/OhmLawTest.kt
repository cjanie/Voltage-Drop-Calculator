package com.cjanie.voltagedropcalculator

import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Resistance
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage
import org.junit.Assert.assertEquals
import org.junit.Test

class VoltageProvider230: VoltageProvider {
    override fun provides(): Voltage {
        return Voltage(230f)
    }

}
class Light(private val consumes: Intensity): Device {
    override fun consumes(): Intensity {
        return consumes
    }
}

interface VoltageProvider {
    fun provides(): Voltage
}

interface Device {
    fun consumes(): Intensity
}

class OhmLaw {
    companion object {
        fun resistance(U: Voltage, I: Intensity): Resistance {
            // Ohm Law: U=RI
            // so R = U/I
            return Resistance(inOhm = U.inVolt / I.inAmpere)
        }

        fun voltage(R: Resistance, I: Intensity): Voltage {
            // Ohm Law: U=RI
            return Voltage(inVolt = R.inOhm * I.inAmpere)
        }

        fun intensity(U: Voltage, R: Resistance): Intensity {
            // Ohm Law: U=RI
            // so I = U/R
            return Intensity(inAmpere = U.inVolt / R.inOhm)
        }
    }
}

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