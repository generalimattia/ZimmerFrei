package com.generals.zimmerfrei.common.extension

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset

fun offsetDateTimeFromLocalDate(localDate: LocalDate) = OffsetDateTime.of(localDate, LocalTime.NOON, ZoneOffset.UTC)