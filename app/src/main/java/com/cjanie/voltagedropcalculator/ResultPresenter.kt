package com.cjanie.voltagedropcalculator

import android.content.Context

class ResultPresenter(private val context: Context) {

    val voltageDropInVoltLabel = context.getString(R.string.voltage_drop_in_volt_label)
    fun voltageDropInVoltAsString(voltageDropInVolt: Float): String {
        return "$voltageDropInVolt ${context.getString(R.string.tension_unit)}"
    }

    val voltageDropPercentageLabel = context.getString(R.string.voltage_drop_percentage_label)
    fun percentageAsString(percentageValue: Float): String {
        return "$percentageValue ${context.getString(R.string.percentage_sign)}"
    }

    fun isVoltageDropAcceptableAsString(isAcceptable: Boolean): String {
        return if (isAcceptable) context.getString(R.string.voltage_drop_acceptable_result)
        else context.getString(R.string.voltage_drop_not_acceptable_result)
    }

    val maxVoltageDropAcceptablePercentageLabel = context.getString(R.string.max_voltage_drop_acceptable_percentage_label)

}