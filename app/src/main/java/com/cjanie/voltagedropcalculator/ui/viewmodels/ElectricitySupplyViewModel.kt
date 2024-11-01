package com.cjanie.voltagedropcalculator.ui.viewmodels

interface ElectricitySupplyViewModel {

    val electricitySupplyLabel: String
    val electricitySupplyOptions: Array<String>

    fun setElectricitySupply(itemPosition: Int)

    fun isElectricitySupplyDefined(): Boolean

}