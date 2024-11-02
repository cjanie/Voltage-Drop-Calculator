package com.cjanie.voltagedropcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.cjanie.voltagedropcalculator.ui.viewmodels.CompleteInstallationViewModel
import com.cjanie.voltagedropcalculator.ui.composables.commons.Header
import com.cjanie.voltagedropcalculator.ui.composables.InstallationDrawing
import com.cjanie.voltagedropcalculator.ui.composables.InstallationSetUpEdition
import com.cjanie.voltagedropcalculator.ui.composables.VoltageDropResult
import com.cjanie.voltagedropcalculator.ui.composables.commons.SubmitButton
import com.cjanie.voltagedropcalculator.ui.theme.VoltageDropCalculatorTheme
import com.cjanie.voltagedropcalculator.ui.theme.paddingMedium
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationSetUpStep

class MainActivity : ComponentActivity() {
    
    private val completeInstallationViewModel by lazy { CompleteInstallationViewModel(application) }

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

                        // Content: installation drawing
                        // 2 modes: edit / result

                        var installationSetUpStep: InstallationSetUpStep? by remember {
                            mutableStateOf(null)
                        }

                        var installation: CompleteInstallationViewModel.InstallationPresenter by remember {
                            mutableStateOf(completeInstallationViewModel.updateInstallationPlaceHolder())
                        }

                        if (installationSetUpStep != null) {
                            InstallationSetUpEdition(
                                completeInstallationViewModel = completeInstallationViewModel,
                                step = installationSetUpStep!!,
                                next = { installationSetUpStep = null
                                        installation = completeInstallationViewModel.updateInstallationPlaceHolder()
                                }
                            )
                        }



                        var voltageDropResult: CompleteInstallationViewModel.VoltageDropResultPresenter? by remember {
                            mutableStateOf(null)
                        }


                        if (installationSetUpStep == null)
                        ConstraintLayout(Modifier.fillMaxSize()) {
                            val (drawing, result) = createRefs()

                            InstallationDrawing(
                                installationPresenter = installation,
                                editionMode = voltageDropResult == null,
                                setInstallationSetUpStep = fun(step: InstallationSetUpStep){
                                    installationSetUpStep = step
                                },
                                modifier = Modifier
                                    .constrainAs(drawing) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(result.top)
                                        width = Dimension.fillToConstraints
                                        height = Dimension.fillToConstraints
                                    }
                                    .fillMaxSize()
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
                                    text = completeInstallationViewModel.calculateVoltageDropLabel,
                                    onClick = { voltageDropResult = completeInstallationViewModel.voltageDropResult() },
                                    enabled = completeInstallationViewModel.isSetUpComplete(),
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