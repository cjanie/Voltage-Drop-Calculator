package com.cjanie.voltagedropcalculator.businesslogic.models.conductor

class Copper: Conductor() {
    override val resistanceReference: ResistanceReference
        get() = ResistanceReference(inOhmByMillimeterSquarePerKilomer = 23.7f)
}