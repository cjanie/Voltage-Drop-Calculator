package com.cjanie.voltagedropcalculator.ui.viewmodels

import android.app.Application
import com.cjanie.voltagedropcalculator.R

class OutputCircuitsViewModel(application: Application) : CableViewModel(application) {

    val skipLabel = application.getString(R.string.skip)
}