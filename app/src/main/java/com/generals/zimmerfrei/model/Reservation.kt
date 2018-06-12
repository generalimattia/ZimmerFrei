package com.generals.zimmerfrei.model

import java.util.*

data class Reservation(val startingDate: Date = Date(),
                       val endDate: Date = Date(),
                       val name: String = "")