package com.cjanie.voltagedropcalculator

import android.content.Context
import com.cjanie.voltagedropcalculator.businesslogic.FunctionalContext





class FormPresenter(val context: Context, val calculatorModel: CalculatorModel) {

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
                Conductor.COPPER -> context.getString(R.string.conductor_copper)
            }

        }.toTypedArray()
    val sectionLabel = context.getString(R.string.section_label)
    val sectionOptions: Array<String> = calculatorModel.sectionValues
        .map {
            section -> "${
                if (section.toString().contains(".0"))
                section.toInt() else section
            } ${context.getString(R.string.conductor_section_unit)}"
        }.toTypedArray()

    val currentIntensityLabel = context.getString(R.string.current_intensity_label)
    val currentIntensityOptions: Array<String> = calculatorModel.currentIntensityValues
        .map {
            intensity ->  "${
                if (intensity.toString().contains(".0"))
                intensity.toInt() else intensity
            } ${context.getString(R.string.current_intensity_unit)}"

        }.toTypedArray()

    val tensionLabel = context.getString(R.string.tension_label)
    val tensionOptions: Array<String> = calculatorModel.tensionValues
        .map {
            tension -> "${
                if (tension.toString().contains(".0"))
                tension.toInt() else tension
            } ${context.getString(R.string.tension_unit)}"
        }.toTypedArray()

    val lengthLabel = context.getString(R.string.line_length_label)
    val lengthUnit = context.getString(R.string.line_length_unit)

    val calculateVoltageDropPercentageLabel = context.getString(R.string.calculate_voltage_drop_label)

    val voltageDropPercentageLabel = context.getString(R.string.voltage_drop_percentage_label)
    fun percentageAsString(percentageValue: Float): String {
        return "$percentageValue ${context.getString(R.string.percentage_sign)}"
    }

    val voltageDropInVoltLabel = context.getString(R.string.voltage_drop_in_volt_label)
    fun voltageDropInVoltAsString(voltageDropInVolt: Float): String {
        return "$voltageDropInVolt ${context.getString(R.string.tension_unit)}"
    }

    fun isVoltageDropAcceptableAsString(isAcceptable: Boolean): String {
        return if (isAcceptable) context.getString(R.string.voltage_drop_acceptable_result)
        else context.getString(R.string.voltage_drop_not_acceptable_result)
    }

}
