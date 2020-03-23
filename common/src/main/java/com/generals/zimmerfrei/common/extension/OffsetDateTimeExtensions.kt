package com.generals.zimmerfrei.common.extension

import org.threeten.bp.*

fun LocalDate.toOffsetDateTime() = OffsetDateTime.of(this, LocalTime.MIDNIGHT, ZoneOffset.UTC)

fun OffsetDateTime.isWeekend(): Boolean = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY