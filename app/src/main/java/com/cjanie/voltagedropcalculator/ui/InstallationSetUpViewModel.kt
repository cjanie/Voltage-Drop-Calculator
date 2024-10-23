package com.cjanie.voltagedropcalculator.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cjanie.voltagedropcalculator.NullValueException
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionnalContext
import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing
import com.cjanie.voltagedropcalculator.businesslogic.factories.LineFactory
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.models.use.Lighting
import com.cjanie.voltagedropcalculator.businesslogic.usecases.InstallationSetUp
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension

enum class InstallationSetUpStep {
    DEFINE_USAGE,
    ADD_INPUT_CABLE,
    ADD_OUTPUT_CIRCUITS,
    DEFINE_NOMINAL_TENSION
}

class InstallationSetUpViewModel(
    private val application: Application
) : AndroidViewModel(application),
    UsageViewModel,
    TensionViewModel
{
    // Title
    val title = application.getString(R.string.installation_setup_title)

    val installationSetUpStart: InstallationSetUpStep? = InstallationSetUpStep.DEFINE_USAGE

    fun next(currentStep: InstallationSetUpStep): InstallationSetUpStep? {
        return when (currentStep) {
            InstallationSetUpStep.DEFINE_USAGE -> InstallationSetUpStep.ADD_INPUT_CABLE
            InstallationSetUpStep.ADD_INPUT_CABLE -> InstallationSetUpStep.ADD_OUTPUT_CIRCUITS
            InstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> InstallationSetUpStep.DEFINE_NOMINAL_TENSION
            InstallationSetUpStep.DEFINE_NOMINAL_TENSION -> null
        }
    }

    fun stepLabel(step: InstallationSetUpStep): String {
        return when (step) {
            InstallationSetUpStep.DEFINE_USAGE -> application.getString(R.string.define_installation_usage_label)
            InstallationSetUpStep.ADD_INPUT_CABLE -> application.getString(R.string.add_input_cable_label)
            InstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> application.getString(R.string.add_output_circuits_label)
            InstallationSetUpStep.DEFINE_NOMINAL_TENSION -> application.getString(R.string.define_nominal_tension)
        }
    }



    // UsageViewModel Impl

    private var functionnalContext: FunctionnalContext? = null
    private val functionnalContextValues: Set<FunctionnalContext> = FunctionnalContext.values().toHashSet()
    override val functionalContextLabel = application.getString(R.string.functional_context_label)
    override val functionnalContextOptions: Array<String> = functionnalContextValues
        .map { functionalContext ->
            when (functionalContext) {
                FunctionnalContext.LIGHTING -> application.getString(R.string.functional_context_lighting)
            }
        }.toTypedArray()
    override fun setFunctionnalContext(itemPosition: Int) {
        functionnalContext = functionnalContextValues.toList()[itemPosition]
    }

    private var electricitySupply: ElectricitySupply? = null
    private val electricitySupplyValues: Set<ElectricitySupply> = ElectricitySupply.values().toHashSet()
    override val electricitySupplyLabel = application.getString(R.string.electricity_supply_label)
    override val electricitySupplyOptions: Array<String> = electricitySupplyValues
        .map { electricitySupply ->
            when (electricitySupply) {
                ElectricitySupply.PUBLIC -> application.getString(R.string.electricity_supply_public)
                ElectricitySupply.PRIVATE -> application.getString(R.string.electricity_supply_private)
            }
        }.toTypedArray()
    override fun setElectricitySupply(itemPosition: Int) {
        electricitySupply = electricitySupplyValues.toList()[itemPosition]
    }

    override fun isUsageDefined(): Boolean {
        return functionnalContext != null && electricitySupply != null
    }


    val inputCableViewModel = InputCableViewModel(application)

    val outputCircuitsViewModel = OutputCircuitsViewModel(application)

    // TensionViewModel Impl
    private var tension: Tension? = null
    private val tensionValues: Set<Tension> = setOf(
        Tension(inVolt = 3f),
        Tension(24f),
        Tension(230f),
    )
    override val tensionLabel = application.getString(R.string.tension_label)
    override val tensionOptions: Array<String> = tensionValues
        .map {
                tension -> "${
            if (tension.inVolt.toString().contains(".0"))
                tension.inVolt.toInt() else tension
        } ${application.getString(R.string.tension_unit)}"
        }.toTypedArray()
    override fun setTension(itemPosition: Int) {
        tension = tensionValues.toList()[itemPosition]
    }

    override fun isTensionDefined(): Boolean {
        return tension != null
    }

    fun setUp(): InstallationSetUp {
        if (functionnalContext == null || electricitySupply == null || tension == null)
            throw NullValueException()
        val use = when (functionnalContext!!) {
            FunctionnalContext.LIGHTING -> Lighting(electricitySupply!!)
        }
        val installationSetUp = InstallationSetUp(use = use, tension = tension!!)
        return installationSetUp
    }

}