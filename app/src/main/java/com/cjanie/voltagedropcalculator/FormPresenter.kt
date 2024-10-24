package com.cjanie.voltagedropcalculator

import android.content.Context
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionnalContext
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing


class FormPresenter(private val context: Context, private val formModel: FormModel) {

    val functionalContextLabel = context.getString(R.string.functional_context_label)
    val functionnalContexts: Array<String> = formModel.functionnalContextValues
        .map { functionalContext ->
            when (functionalContext) {
                FunctionnalContext.LIGHTING -> context.getString(R.string.functional_context_lighting)
                FunctionnalContext.MOTOR -> "Motor"
            }
        }.toTypedArray()
    fun setFunctionnalContext(itemPosition: Int) {
        formModel.setFunctionalContext(itemPosition)
    }

    val electricitySupplyLabel = context.getString(R.string.electricity_supply_label)
    val electricitySupplyOptions: Array<String> = formModel.electricitySupplyValues
        .map { electricitySupply ->
            when (electricitySupply) {
                ElectricitySupply.PUBLIC -> context.getString(R.string.electricity_supply_public)
                ElectricitySupply.PRIVATE -> context.getString(R.string.electricity_supply_private)
            }
        }.toTypedArray()
    fun setElectricitySupply(itemPosition: Int) {
        formModel.setElectricitySupply(itemPosition)
    }

    val phasingLabel = context.getString(R.string.phasing_label)
    val phasingOptions : Array<String> = formModel.phasingValues
        .map { phasing ->
            when (phasing) {
                Phasing.SINGLE_PHASE -> context.getString(R.string.phasing_single_phase)
                Phasing.THREE_PHASE -> context.getString(R.string.phasing_three_phase)
            }
        }.toTypedArray()
    fun setPhasing(itemPosition: Int) {
        formModel.setPhasing(itemPosition)
    }

    val conductorLabel = context.getString(R.string.conductor_label)
    val conductorOptions: Array<String> = formModel.conductorMaterialValues
        .map { conductorMaterial ->
            when (conductorMaterial) {
                ConductorMaterial.COPPER -> context.getString(R.string.conductor_copper)
            }

        }.toTypedArray()
    fun setConductor(itemPosition: Int) {
        formModel.setConductor(itemPosition)
    }

    val sectionLabel = context.getString(R.string.section_label)
    val sectionOptions: Array<String> = formModel.sectionValues
        .map {
            section -> "${
                if (section.inMillimeterSquare.toString().contains(".0"))
                section.inMillimeterSquare.toInt() else section.inMillimeterSquare
            } ${context.getString(R.string.conductor_section_unit)}"
        }.toTypedArray()
    fun setSection(itemPosition: Int) {
        formModel.setSection(itemPosition)
    }

    val intensityLabel = context.getString(R.string.current_intensity_label)
    val intensityOptions: Array<String> = formModel.intensityValues
        .map {
            intensity ->  "${
                if (intensity.inAmpere.toString().contains(".0"))
                intensity.inAmpere.toInt() else intensity
            } ${context.getString(R.string.current_intensity_unit)}"

        }.toTypedArray()
    fun setIntensity(itemPosition: Int) {
        formModel.setIntensity(itemPosition)
    }

    val tensionLabel = context.getString(R.string.tension_label)
    val tensionOptions: Array<String> = formModel.tensionValues
        .map {
            tension -> "${
                if (tension.inVolt.toString().contains(".0"))
                tension.inVolt.toInt() else tension
            } ${context.getString(R.string.tension_unit)}"
        }.toTypedArray()
    fun setTension(itemPosition: Int) {
        formModel.setTension(itemPosition)
    }

    val lengthLabel = context.getString(R.string.line_length_label)
    val lengthUnit = context.getString(R.string.line_length_unit)
    fun setLength(inKilometer: Float) {
        formModel.setLength(inKilometer)
    }

    fun isFormComplete(): Boolean {
        return !formModel.isNullValue()
    }


    val calculateVoltageDropLabel = context.getString(R.string.calculate_voltage_drop_label)
    fun calculateVoltageDrop(): FormModel.VoltageDropResult {
        return formModel.calculateVoltageDrop()
    }

}
