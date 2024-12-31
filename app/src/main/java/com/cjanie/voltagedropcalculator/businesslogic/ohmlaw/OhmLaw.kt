package com.cjanie.voltagedropcalculator.businesslogic.ohmlaw

import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Resistance
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage

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