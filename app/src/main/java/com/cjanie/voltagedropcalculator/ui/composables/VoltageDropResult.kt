package com.cjanie.voltagedropcalculator.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cjanie.voltagedropcalculator.ui.composables.commons.LabeledText
import com.cjanie.voltagedropcalculator.ui.composables.commons.Title
import com.cjanie.voltagedropcalculator.ui.theme.onWhiteColor
import com.cjanie.voltagedropcalculator.ui.theme.whiteColor
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationViewModel

@Composable
fun VoltageDropResult(voltageDropResultPresenter: InstallationViewModel.VoltageDropResultPresenter, modifier: Modifier = Modifier.fillMaxWidth()) {
    Column(
        modifier = modifier
            .background(voltageDropResultPresenter.warningColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .background(whiteColor),
            horizontalAlignment = Alignment.CenterHorizontally
        )  {
            LabeledText(
                label = voltageDropResultPresenter.inVoltLabel,
                text = voltageDropResultPresenter.inVoltValue,
                textColor = onWhiteColor
            )
            LabeledText(
                label = voltageDropResultPresenter.asPercentageLabel,
                text = voltageDropResultPresenter.asPercentageValue,
                textColor = onWhiteColor
            )
        }

        Title(
            text = voltageDropResultPresenter.isVoltageDropAcceptableWarningText,
            textColor = voltageDropResultPresenter.onWarningColor
        )

        LabeledText(
            label = voltageDropResultPresenter.maxVoltageDropLimitPercentageLabel,
            text = voltageDropResultPresenter.maxVoltageDropLimitPercentageValue,
            textColor = voltageDropResultPresenter.onWarningColor
        )
    }
}