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
import com.cjanie.voltagedropcalculator.ui.viewmodels.InstallationViewModel
import com.cjanie.voltagedropcalculator.ui.composables.commons.Header
import com.cjanie.voltagedropcalculator.ui.composables.InstallationDrawing
import com.cjanie.voltagedropcalculator.ui.composables.InstallationSetUp
import com.cjanie.voltagedropcalculator.ui.composables.VoltageDropResult
import com.cjanie.voltagedropcalculator.ui.composables.commons.SubmitButton
import com.cjanie.voltagedropcalculator.ui.theme.VoltageDropCalculatorTheme
import com.cjanie.voltagedropcalculator.ui.theme.paddingMedium

class MainActivity : ComponentActivity() {
    
    private val installationViewModel by lazy { InstallationViewModel(application) }

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

                        var installation: InstallationViewModel.InstallationPresenter by remember {
                            mutableStateOf(installationViewModel.installationPlaceHolder())
                        }
                        var voltageDropResult: InstallationViewModel.VoltageDropResultPresenter? by remember {
                            mutableStateOf(null)
                        }


                        ConstraintLayout(Modifier.fillMaxSize()) {
                            val (drawing, result) = createRefs()

                            InstallationDrawing(
                                installationPresenter = installation!!,
                                editionMode = voltageDropResult == null,
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
                                    text = installationViewModel.calculateVoltageDropLabel,
                                    onClick = { voltageDropResult = installationViewModel.voltageDropResult() },
                                    enabled = installationViewModel.isSetUpComplete(),
                                    modifier = Modifier.constrainAs(result) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom)
                                    }.padding(paddingMedium)
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