package com.cjanie.voltagedropcalculator.ui.viewmodels

import android.app.Application
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.models.TruncatedInstallation
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.VoltageDrop

enum class TruncatedInstallationSetUpStep {
    DEFINE_ELECTRICITY_SUPPLY,
    DEFINE_NOMINAL_TENSION,
    DEFINE_INPUT_CABLE_VOLTAGE_DROP,
    ADD_OUTPUT_CIRCUITS,
    DEFINE_USAGE
}

class TruncatedInstallationSetUpViewModel(private val application: Application) :
    InstallationSetUpViewModel(application) {

    companion object {
        fun installationSetUpStepLabel(step: TruncatedInstallationSetUpStep, application: Application): String {
            return when (step) {
                TruncatedInstallationSetUpStep.DEFINE_USAGE -> application.getString(R.string.define_installation_usage_label)
                TruncatedInstallationSetUpStep.DEFINE_ELECTRICITY_SUPPLY -> application.getString(R.string.define_electricity_supply)
                TruncatedInstallationSetUpStep.DEFINE_NOMINAL_TENSION -> application.getString(R.string.define_nominal_tension)
                TruncatedInstallationSetUpStep.DEFINE_INPUT_CABLE_VOLTAGE_DROP -> application.getString(R.string.define_input_cable_voltage_drop)
                TruncatedInstallationSetUpStep.ADD_OUTPUT_CIRCUITS -> application.getString(R.string.add_output_circuits_label)
                TruncatedInstallationSetUpStep.DEFINE_USAGE -> application.getString(R.string.define_installation_usage_label)

            }
        }
    }

    fun installationSetUpStepLabel(step: TruncatedInstallationSetUpStep): String {
        return installationSetUpStepLabel(step, application)
    }

    fun updateInstallationPlaceHolder(): TruncatedInstallationPresenter {
        return TruncatedInstallationPresenter(
            installation = TruncatedInstallation(
                usage = Usage.MOTOR,
                electricitySupply = ElectricitySupply.PRIVATE,
                nominalTension = Tension(inVolt = 400f),
                inputCableVoltageDrop = VoltageDrop(inVolt = 14f)
            ),
            application = application
        )
    }

    class TruncatedInstallationPresenter(
        installation: TruncatedInstallation,
        application: Application,
    ) : InstallationPresenter() {

        override val usageAsString = usageToString(usage = installation.usage, application = application)
        override val usage: Usage = installation.usage
        override val electricitySupply = electricitySupplyToString(installation.electricitySupply, application)
        override val tension = tensionToString(tension = installation.nominalTension, application = application)
        val inputCableVoltageDrop = tensionToString(tension = Tension(installation.inputCableVoltageDrop.inVolt), application = application)
    }

}