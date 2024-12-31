package com.cjanie.voltagedropcalculator.businesslogic.voltagedrop

import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.ConductorResistance
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.ConductorVoltageDropProperty
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.LinearReactance
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.LinearResistance
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.PhaseShift
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section

class Copper {

    companion object {
        val RESISTANCE = ConductorResistance(
            inOhmPerMilimeterSquareAndPerKilometer = 23.7f
        )

        fun linearResistance(section: Section): LinearResistance {
            return LinearResistance(
                inOhmPerKilometer =
                RESISTANCE.inOhmPerMilimeterSquareAndPerKilometer
                        / section.inMillimeterSquare
            )
        }

        val DEFAULT_LINEAR_REACTANCE = LinearReactance(inOhmPerKilometer = 0.8f)

        fun voltageDropProperty(
            section: Section,
            X: LinearReactance = DEFAULT_LINEAR_REACTANCE,
            usage: Usage = Usage.LIGHTING
        ): ConductorVoltageDropProperty {

            val R = linearResistance(section)
            val cosPhi = when (usage) {
                Usage.LIGHTING -> 1f
                Usage.MOTOR -> 0.8f
            }
            val sinPhi = PhaseShift.sinPhiFrom(cosPhi)

            return ConductorVoltageDropProperty(inVoltPerAmpereAndPerKilometer =
            2 * (R.inOhmPerKilometer * cosPhi + X.inOhmPerKilometer * sinPhi)
            )
        }

    }

}