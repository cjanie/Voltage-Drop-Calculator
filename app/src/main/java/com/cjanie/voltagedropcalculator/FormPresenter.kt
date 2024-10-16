package com.cjanie.voltagedropcalculator

import android.content.Context
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionnalContext
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing


class FormPresenter(private val context: Context, private val calculatorModel: CalculatorModel) {

    val functionalContextLabel = context.getString(R.string.functional_context_label)
    val functionnalContexts: Array<String> = calculatorModel.functionnalContextValues
        .map { functionalContext ->
            when (functionalContext) {
                FunctionnalContext.LIGHTING -> context.getString(R.string.functional_context_lighting)
            }
        }.toTypedArray()
    fun setFunctionnalContext(itemPosition: Int) {
        calculatorModel.setFunctionalContext(itemPosition)
    }

    val electricitySupplyLabel = context.getString(R.string.electricity_supply_label)
    val electricitySupplyOptions: Array<String> = calculatorModel.electricitySupplyValues
        .map { electricitySupply ->
            when (electricitySupply) {
                ElectricitySupply.PUBLIC -> context.getString(R.string.electricity_supply_public)
                ElectricitySupply.PRIVATE -> context.getString(R.string.electricity_supply_private)
            }
        }.toTypedArray()
    fun setElectricitySupply(itemPosition: Int) {
        calculatorModel.setElectricitySupply(itemPosition)
    }

    val phasingLabel = context.getString(R.string.phasing_label)
    val phasingOptions : Array<String> = calculatorModel.phasingValues
        .map { phasing ->
            when (phasing) {
                Phasing.SINGLE_PHASE -> context.getString(R.string.phasing_single_phase)
                Phasing.THREE_PHASE -> context.getString(R.string.phasing_three_phase)
            }
        }.toTypedArray()
    fun setPhasing(itemPosition: Int) {
        calculatorModel.setPhasing(itemPosition)
    }

    val conductorLabel = context.getString(R.string.conductor_label)
    val conductorOptions: Array<String> = calculatorModel.conductorMaterialValues
        .map { conductorMaterial ->
            when (conductorMaterial) {
                ConductorMaterial.COPPER -> context.getString(R.string.conductor_copper)
            }

        }.toTypedArray()
    fun setConductor(itemPosition: Int) {
        calculatorModel.setConductor(itemPosition)
    }

    val sectionLabel = context.getString(R.string.section_label)
    val sectionOptions: Array<String> = calculatorModel.sectionValues
        .map {
            section -> "${
                if (section.inMillimeterSquare.toString().contains(".0"))
                section.inMillimeterSquare.toInt() else section.inMillimeterSquare
            } ${context.getString(R.string.conductor_section_unit)}"
        }.toTypedArray()
    fun setSection(itemPosition: Int) {
        calculatorModel.setSection(itemPosition)
    }

    val intensityLabel = context.getString(R.string.current_intensity_label)
    val intensityOptions: Array<String> = calculatorModel.intensityValues
        .map {
            intensity ->  "${
                if (intensity.inAmpere.toString().contains(".0"))
                intensity.inAmpere.toInt() else intensity
            } ${context.getString(R.string.current_intensity_unit)}"

        }.toTypedArray()
    fun setIntensity(itemPosition: Int) {
        calculatorModel.setIntensity(itemPosition)
    }

    val tensionLabel = context.getString(R.string.tension_label)
    val tensionOptions: Array<String> = calculatorModel.tensionValues
        .map {
            tension -> "${
                if (tension.inVolt.toString().contains(".0"))
                tension.inVolt.toInt() else tension
            } ${context.getString(R.string.tension_unit)}"
        }.toTypedArray()
    fun setTension(itemPosition: Int) {
        calculatorModel.setTension(itemPosition)
    }

    val lengthLabel = context.getString(R.string.line_length_label)
    val lengthUnit = context.getString(R.string.line_length_unit)
    fun setLength(inKilometer: Float) {
        calculatorModel.setLength(inKilometer)
    }

    fun isFormComplete(): Boolean {
        return !calculatorModel.isNullValue()
    }


    val calculateVoltageDropLabel = context.getString(R.string.calculate_voltage_drop_label)
    fun calculateVoltageDrop(): CalculatorModel.VoltageDropResult {
        return calculatorModel.calculateVoltageDrop()
    }

}
