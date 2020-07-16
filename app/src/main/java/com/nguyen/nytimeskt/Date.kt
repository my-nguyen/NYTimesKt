package com.nguyen.nytimeskt

import java.io.Serializable
import java.util.*

class Date(val year: Int, val month: Int, val day: Int) : Serializable {

    constructor(date: GregorianCalendar): this(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))

    override fun toString(): String {
        return year.toString() + String.format("%02d", month + 1) + String.format("%02d", day)
    }
}
