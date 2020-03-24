package com.generals.zimmerfrei.common.extension

import org.threeten.bp.*

fun LocalDate.isWeekend(): Boolean = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY