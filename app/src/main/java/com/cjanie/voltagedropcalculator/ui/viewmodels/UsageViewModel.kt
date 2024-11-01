package com.cjanie.voltagedropcalculator.ui.viewmodels

interface UsageViewModel {

    val usageLabel: String
    val usageOptions: Array<String>

    fun setUsage(itemPosition: Int)

}