package com.cjanie.voltagedropcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cjanie.voltagedropcalculator.ui.composables.AddOutputCircuits
import com.cjanie.voltagedropcalculator.ui.composables.EditElectricitySupply
import com.cjanie.voltagedropcalculator.ui.composables.EditInputCableVoltageDrop
import com.cjanie.voltagedropcalculator.ui.composables.EditNominalTension
import com.cjanie.voltagedropcalculator.ui.composables.EditUsage
import com.cjanie.voltagedropcalculator.ui.viewmodels.CompleteInstallationSetUpViewModel
import com.cjanie.voltagedropcalculator.ui.composables.commons.Header
import com.cjanie.voltagedropcalculator.ui.composables.InstallationDrawing
import com.cjanie.voltagedropcalculator.ui.composables.InstallationSetUpEdition
import com.cjanie.voltagedropcalculator.ui.composables.InstallationTemplate
import com.cjanie.voltagedropcalculator.ui.composables.TruncatedInstallationSetUpEdition
import com.cjanie.voltagedropcalculator.ui.composables.VoltageDropResult
import com.cjanie.voltagedropcalculator.ui.composables.commons.SubmitButton
import com.cjanie.voltagedropcalculator.ui.theme.VoltageDropCalculatorTheme
import com.cjanie.voltagedropcalculator.ui.theme.paddingMedium
import com.cjanie.voltagedropcalculator.ui.viewmodels.CompleteInstallationSetUpStep
import com.cjanie.voltagedropcalculator.ui.viewmodels.TruncatedInstallationSetUpStep
import com.cjanie.voltagedropcalculator.ui.viewmodels.TruncatedInstallationSetUpViewModel

class MainActivity : ComponentActivity() {
    
    private val completeInstallationSetUpViewModel by lazy { CompleteInstallationSetUpViewModel(application) }
    private val truncatedInstallationSetUpViewModel by lazy { TruncatedInstallationSetUpViewModel(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            VoltageDropCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ){
                        Header(
                            text = getString(R.string.app_name)
                        )

                        var template by remember {
                            mutableStateOf(InstallationTemplate.TRUNCATED)
                        }
                        Button(onClick = {
                            when (template) {
                                InstallationTemplate.TRUNCATED -> template = InstallationTemplate.COMPLETE
                                InstallationTemplate.COMPLETE -> template = InstallationTemplate.TRUNCATED
                            }
                        }) {

                        }

                        // Content: installation drawing
                        // 2 modes: edit / result

                        var truncatedInstallationSetUpStep: TruncatedInstallationSetUpStep? by remember {
                            mutableStateOf(null)
                        }

                        var completeInstallationSetUpStep: CompleteInstallationSetUpStep? by remember {
                            mutableStateOf(null)
                        }

                        var installation: CompleteInstallationSetUpViewModel.CompleteInstallationPresenter by remember {
                            mutableStateOf(completeInstallationSetUpViewModel.updateInstallationPlaceHolder())
                        }

                        var truncatedInstallation: TruncatedInstallationSetUpViewModel.TruncatedInstallationPresenter by remember {
                            mutableStateOf(truncatedInstallationSetUpViewModel.updateInstallationPlaceHolder())
                        }

                        if(truncatedInstallationSetUpStep != null) {

                            fun next() {
                                truncatedInstallationSetUpStep = null
                                truncatedInstallation = truncatedInstallationSetUpViewModel
                                    .updateInstallationPlaceHolder()
                            }

                            TruncatedInstallationSetUpEdition(
                                viewModel = truncatedInstallationSetUpViewModel,
                                step = truncatedInstallationSetUpStep!!,
                                next = { next() }
                            )
                        }

                        if (completeInstallationSetUpStep != null) {
                            InstallationSetUpEdition(
                                completeInstallationSetUpViewModel = completeInstallationSetUpViewModel,
                                step = completeInstallationSetUpStep!!,
                                next = { completeInstallationSetUpStep = null
                                        installation = completeInstallationSetUpViewModel.updateInstallationPlaceHolder()
                                }
                            )
                        }



                        var voltageDropResult: CompleteInstallationSetUpViewModel.VoltageDropResultPresenter? by remember {
                            mutableStateOf(null)
                        }


                        if (completeInstallationSetUpStep == null)
                        ConstraintLayout(Modifier.fillMaxSize()) {
                            val (drawing, result) = createRefs()

                            InstallationDrawing(
                                completeInstallationPresenter = installation,
                                truncatedInstallationPresenter = truncatedInstallation,
                                editionMode = voltageDropResult == null,
                                setInstallationSetUpStep = fun(step: CompleteInstallationSetUpStep){
                                    completeInstallationSetUpStep = step
                                },
                                installationTemplate = template,
                                modifier = Modifier
                                    .constrainAs(drawing) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(result.top)
                                        width = Dimension.fillToConstraints
                                        height = Dimension.fillToConstraints
                                    }
                                    .fillMaxSize(),
                                setTruncatedInstallationSetUpStep = fun (step: TruncatedInstallationSetUpStep) {
                                    truncatedInstallationSetUpStep = step
                                }
                            )


                            if(voltageDropResult != null) {
                                VoltageDropResult(
                                    voltageDropResultPresenter = voltageDropResult!!,
                                    modifier = Modifier.constrainAs(result) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom)
                                    }
                                )
                            } else {
                                SubmitButton(
                                    text = completeInstallationSetUpViewModel.calculateVoltageDropLabel,
                                    onClick = { voltageDropResult = completeInstallationSetUpViewModel.voltageDropResult() },
                                    enabled = completeInstallationSetUpViewModel.isSetUpComplete(),
                                    modifier = Modifier
                                        .constrainAs(result) {
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom)
                                        }
                                        .padding(paddingMedium)
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VoltageDropCalculatorTheme {

    }
}