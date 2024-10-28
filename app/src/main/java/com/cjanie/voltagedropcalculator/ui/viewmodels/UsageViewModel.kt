package com.cjanie.voltagedropcalculator.ui.viewmodels

interface UsageViewModel {

    val functionalContextLabel: String
    val functionnalContextOptions: Array<String>

    val electricitySupplyLabel: String
    val electricitySupplyOptions: Array<String>

    fun setFunctionnalContext(itemPosition: Int)
    fun setElectricitySupply(itemPosition: Int)

    fun isUsageDefined(): Boolean
}