package com.cjanie.voltagedropcalculator.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cjanie.voltagedropcalculator.NullValueException
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.Usage
import com.cjanie.voltagedropcalculator.businesslogic.factories.LineFactory
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Tension

open class InstallationSetUpViewModel(application: Application) : AndroidViewModel(application),
    UsageViewModel,
    ElectricitySupplyViewModel,
    TensionViewModel {

    companion object {

        fun usageToString(usage: Usage, application: Application): String {
            return when (usage) {
                Usage.LIGHTING -> application.getString(R.string.functional_context_lighting)
                Usage.MOTOR -> application.getString(R.string.functional_context_motor)
            }
        }

        fun electricitySupplyToString(electricitySupply: ElectricitySupply, application: Application): String {
            return when (electricitySupply) {
                ElectricitySupply.PRIVATE -> application.getString(R.string.electricity_supply_private)
                ElectricitySupply.PUBLIC -> application.getString(R.string.electricity_supply_public)
            }
        }

        fun tensionToString(tension: Tension, application: Application): String {
            return "${tension.inVolt.toInt()} ${application.getString(R.string.tension_unit)}"
        }

    }

    // UsageViewModel Impl

    protected var usage: Usage = Usage.LIGHTING
    private val usageValues: Set<Usage> = Usage.values().toHashSet()
    override val usageLabel = application.getString(R.string.usage_label)
    override val usageOptions: Array<String> = usageValues
        .map { usageToString(it, application) }.toTypedArray()
    override fun setUsage(itemPosition: Int) {
        usage = usageValues.toList()[itemPosition]
    }

    // ElectricitySupplyViewModel Impl
    protected var electricitySupply: ElectricitySupply = ElectricitySupply.PUBLIC
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

    override fun isElectricitySupplyDefined(): Boolean {
        return electricitySupply != null
    }

    // TensionViewModel Impl
    private val tensionValues: Set<Tension> = setOf(
        Tension(inVolt = 3f),
        Tension(24f),
        Tension(230f),
    )
    protected var tension: Tension = tensionValues.toList()[2]

    override val tensionLabel = application.getString(R.string.tension_label)
    override val tensionOptions: Array<String> = tensionValues
        .map { tensionToString(it, application) }.toTypedArray()
    override fun setTension(itemPosition: Int) {
        tension = tensionValues.toList()[itemPosition]
    }

    override fun isTensionDefined(): Boolean {
        return tension != null
    }

    val outputCircuitsViewModel = OutputCircuitsViewModel(application)

    protected fun createCable(cableViewModel: CableViewModel): Line {
        if(!cableViewModel.isFormComplete())
            throw NullValueException()
        return LineFactory.line(
            usage = usage,
            electricitySupply = electricitySupply,
            phasing = cableViewModel.phasing!!,
            conductorMaterial = cableViewModel.conductor!!,
            section = cableViewModel.section!!,
            intensity = cableViewModel.intensity!!,
            length = cableViewModel.length!!
        )
    }

    // Installation Presenter interfaces
    interface UsagePresenter {
        val usageAsString: String
        val usage: Usage

    }

    interface ElectricitySupplyPresenter {
        val electricitySupply: String
    }

    interface TensionPresenter {
        val tension: String
    }

    abstract class InstallationPresenter : UsagePresenter, ElectricitySupplyPresenter, TensionPresenter

}