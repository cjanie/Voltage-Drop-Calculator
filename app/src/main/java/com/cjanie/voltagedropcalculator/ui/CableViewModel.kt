package com.cjanie.voltagedropcalculator.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cjanie.voltagedropcalculator.NullValueException
import com.cjanie.voltagedropcalculator.R
import com.cjanie.voltagedropcalculator.businesslogic.enums.ConductorMaterial
import com.cjanie.voltagedropcalculator.businesslogic.enums.ElectricitySupply
import com.cjanie.voltagedropcalculator.businesslogic.enums.FunctionnalContext
import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing
import com.cjanie.voltagedropcalculator.businesslogic.factories.LineFactory
import com.cjanie.voltagedropcalculator.businesslogic.models.line.Line
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Section

open class CableViewModel(application: Application) {


    val phasingLabel = application.getString(R.string.phasing_label)
    private val phasingValues: Set<Phasing> = Phasing.values().toHashSet()
    val phasingOptions : Array<String> = phasingValues
        .map { phasing ->
            when (phasing) {
                Phasing.SINGLE_PHASE -> application.getString(R.string.phasing_single_phase)
                Phasing.THREE_PHASE -> application.getString(R.string.phasing_three_phase)
            }
        }.toTypedArray()

    val conductorLabel = application.getString(R.string.conductor_label)
    val conductorMaterialValues: Set<ConductorMaterial> = ConductorMaterial.values().toHashSet()
    val conductorOptions: Array<String> = conductorMaterialValues
        .map { conductorMaterial ->
            when (conductorMaterial) {
                ConductorMaterial.COPPER -> application.getString(R.string.conductor_copper)
            }
        }.toTypedArray()

    val sectionLabel = application.getString(R.string.section_label)
    val sectionValues: Set<Section> = setOf(
        Section(inMillimeterSquare = 1.5f),
        Section(inMillimeterSquare = 2.5f),
        Section(inMillimeterSquare = 4f),
        Section(inMillimeterSquare = 6f),
        Section(inMillimeterSquare = 10f),
        Section(inMillimeterSquare = 16f),
        Section(inMillimeterSquare = 25f),
        Section(inMillimeterSquare = 35f),
        Section(inMillimeterSquare = 50f),
        Section(inMillimeterSquare = 70f),
        Section(inMillimeterSquare = 95f),
        Section(inMillimeterSquare = 120f),
        Section(inMillimeterSquare = 150f),
        Section(inMillimeterSquare = 185f),
        Section(inMillimeterSquare = 240f),
        Section(inMillimeterSquare = 300f)
    )
    val sectionOptions: Array<String> = sectionValues
        .map {
                section -> "${
            if (section.inMillimeterSquare.toString().contains(".0"))
                section.inMillimeterSquare.toInt() else section.inMillimeterSquare
        } ${application.getString(R.string.conductor_section_unit)}"
        }.toTypedArray()

    val intensityLabel = application.getString(R.string.current_intensity_label)
    val intensityValues: Set<Intensity> = setOf(
        Intensity(inAmpere = 20f),
        Intensity(inAmpere = 100f),
        Intensity(inAmpere = 150f),
        Intensity(inAmpere = 500f)
    )
    val intensityOptions: Array<String> = intensityValues
        .map {
                intensity ->  "${
            if (intensity.inAmpere.toString().contains(".0"))
                intensity.inAmpere.toInt() else intensity
        } ${application.getString(R.string.current_intensity_unit)}"

        }.toTypedArray()

    val lengthLabel = application.getString(R.string.line_length_label)
    val lengthUnit = application.getString(R.string.line_length_unit)

    var phasing: Phasing? = null
    var conductor: ConductorMaterial? = null
    var section: Section? = null
    var intensity: Intensity? = null
    var length: Length? = null

    fun setPhasing(itemPosition: Int) {
        phasing = phasingValues.toList()[itemPosition]
    }

    fun setConductor(itemPosition: Int) {
        conductor = conductorMaterialValues.toList()[itemPosition]
    }

    fun setSection(itemPosition: Int) {
        section = sectionValues.toList()[itemPosition]
    }

    fun setIntensity(itemPosition: Int) {
        intensity = intensityValues.toList()[itemPosition]
    }

    fun setLength(inKilometer: Float) {
        length = Length(inKilometer)
    }

    fun isFormComplete(): Boolean {
        return !isNullValue()
    }

    private fun isNullValue(): Boolean {
        return phasing == null ||
                conductor == null ||
                section == null ||
                intensity == null ||
                length == null
    }

    fun createCable(functionnalContext: FunctionnalContext, electricitySupply: ElectricitySupply): Line {
        if (isNullValue()) throw NullValueException()
        return LineFactory.line(functionnalContext, electricitySupply, phasing!!, conductor!!, section!!, intensity!!, length!!)
    }

}