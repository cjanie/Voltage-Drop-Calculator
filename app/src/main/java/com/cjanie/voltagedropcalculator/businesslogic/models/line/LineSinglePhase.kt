package com.cjanie.voltagedropcalculator.businesslogic.models.line

import com.cjanie.voltagedropcalculator.businesslogic.enums.Phasing
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.PhaseShift
import com.cjanie.voltagedropcalculator.businesslogic.models.conductor.Conductor
import com.cjanie.voltagedropcalculator.businesslogic.valueobjects.Intensity
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Length
import com.cjanie.voltagedropcalculator.businesslogic.voltagedrop.valueobjects.Section

class LineSinglePhase(
    phaseShift: PhaseShift,
    conductor: Conductor,
    section: Section,
    intensity: Intensity,
    length: Length
) : Line(phaseShift, conductor, section, intensity, length) {

    override val phasing: Phasing
        get() = Phasing.SINGLE_PHASE

}
