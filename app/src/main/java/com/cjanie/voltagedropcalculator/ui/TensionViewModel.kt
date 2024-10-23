package com.cjanie.voltagedropcalculator.ui

interface TensionViewModel {

    val tensionLabel: String
    val tensionOptions: Array<String>
    fun setTension(itemPosition: Int)
    fun isTensionDefined(): Boolean
}