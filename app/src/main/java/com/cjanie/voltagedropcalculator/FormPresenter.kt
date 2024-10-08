package com.cjanie.voltagedropcalculator

import android.content.Context
import com.cjanie.voltagedropcalculator.businesslogic.FunctionalContext
import com.cjanie.voltagedropcalculator.businesslogic.Material


class FormPresenter(private val context: Context, calculatorModel: CalculatorModel) {

    val functionalContextLabel = context.getString(R.string.functional_context_label)
    val functionalContexts: Array<String> = calculatorModel.functionalContextValues
        .map { functionalContext ->
            when (functionalContext) {
                FunctionalContext.LIGHTING -> context.getString(R.string.functional_context_lighting)
            }
        }.toTypedArray()

    val lineTypeLabel = context.getString(R.string.line_type_label)
    val lineTypeOptions : Array<String> = calculatorModel.lineTypeValues
        .map { lineType ->
            when (lineType) {
                LineType.SINGLE_PHASE -> context.getString(R.string.line_type_single_phase)
                LineType.THREE_PHASE -> context.getString(R.string.line_type_three_phase)
            }
        }.toTypedArray()

    val conductorLabel = context.getString(R.string.conductor_label)
    val conductorOptions: Array<String> = calculatorModel.conductorValues
        .map { conductorMaterial ->
            when (conductorMaterial) {
                Material.COPPER -> context.getString(R.string.conductor_copper)
            }

        }.toTypedArray()
    val sectionLabel = context.getString(R.string.section_label)
    val sectionOptions: Array<String> = calculatorModel.sectionValues
        .map {
            section -> "${
                if (section.inMillimeterSquare.toString().contains(".0"))
                section.inMillimeterSquare.toInt() else section
            } ${context.getString(R.string.conductor_section_unit)}"
        }.toTypedArray()

    val currentIntensityLabel = context.getString(R.string.current_intensity_label)
    val currentIntensityOptions: Array<String> = calculatorModel.currentIntensityValues
        .map {
            intensity ->  "${
                if (intensity.inAmpere.toString().contains(".0"))
                intensity.inAmpere.toInt() else intensity
            } ${context.getString(R.string.current_intensity_unit)}"

        }.toTypedArray()

    val tensionLabel = context.getString(R.string.tension_label)
    val tensionOptions: Array<String> = calculatorModel.tensionValues
        .map {
            tension -> "${
                if (tension.inVolt.toString().contains(".0"))
                tension.inVolt.toInt() else tension
            } ${context.getString(R.string.tension_unit)}"
        }.toTypedArray()

    val lengthLabel = context.getString(R.string.line_length_label)
    val lengthUnit = context.getString(R.string.line_length_unit)

    val calculateVoltageDropPercentageLabel = context.getString(R.string.calculate_voltage_drop_label)

}
