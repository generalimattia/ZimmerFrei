package com.generals.zimmerfrei.common.extension

import org.threeten.bp.*

fun offsetDateTimeFromLocalDate(localDate: LocalDate) = OffsetDateTime.of(localDate, LocalTime.NOON, ZoneOffset.UTC)

fun OffsetDateTime.isWeekend(): Boolean = (dayOfWeek == DayOfWeek.SATURDAY) || (dayOfWeek == DayOfWeek.SUNDAY)