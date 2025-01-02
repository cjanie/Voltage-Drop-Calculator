package com.cjanie.voltagedropcalculator.ui.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Voltage
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.VoltageDropCalculator
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.VoltageDrop
import com.cjanie.voltagedropcalculator.ui.theme.greenWarningColor
import com.cjanie.voltagedropcalculator.ui.theme.redWarningColor

class VoltageDropViewModel: ViewModel() {

    val usage = Usage.LIGHTING

    // Conductor
    private val conductorMaterial = ConductorMaterial.COPPER
    var S = Section(inMillimeterSquare = 2.5f)

    // Voltage Drop at end of line
    var I = Intensity(inAmpere = 20f)

    var L = Length(inKilometer = 0.02f)

    private fun voltageDropAtEndOfLine(): VoltageDrop {
        return VoltageDropCalculator(
            conductorMaterial = conductorMaterial,
            S = S,
            usage = usage,
            I = I,
            L = L
        ).voltageDropAtEndOfLine
    }

    // acceptable depending on context nominal U and usage
    var nominal_U = Voltage(inVolt = 230f)

    private fun isVoltageDropAcceptable(): Boolean {
        return voltageDropAtEndOfLine().isVoltageDropAcceptable(nominal_U, usage)
    }


    // Device Icon
    val deviceIconId = when (usage) {
        Usage.LIGHTING -> R.drawable.ic_lightbulb_outline_24
        Usage.MOTOR -> R.drawable.ic_auto_mode_24
    }

    // Texts
    val materialText = conductorMaterial.toString()

    val sectionUnit = Section.unit
    fun sectionText(): String {
        return "$sectionUnit ${S.inMillimeterSquare}"
    }

    fun intensityText(): String {
        return "${Intensity.unit} ${I.inAmpere}"
    }

    val lengthUnit = Length.unit
    fun lengthText(): String {
        return "$lengthUnit ${L.inKilometer}"
    }

    val voltageDropUnit = VoltageDrop.unit
    fun voltageDropText(): String {
        return "$voltageDropUnit ${voltageDropAtEndOfLine().inVolt}"
    }

    fun warningColor(): Color {
        return if (isVoltageDropAcceptable()) greenWarningColor else redWarningColor
    }

    fun nominalVoltageText(): String {
        return "Nominal voltage ${Voltage.unit} ${nominal_U.inVolt}"
    }

    fun voltageDropPercentageText(): String {
        val delta_U = voltageDropAtEndOfLine()
        val voltageDropPercentage = delta_U.percentage(nominal_U)
        return "Max Voltage Drop % accepted ${VoltageDrop.maxPercentageAcceptable(usage)}. Current % $voltageDropPercentage."
    }



}