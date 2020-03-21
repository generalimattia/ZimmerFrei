package com.generals.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class LocalDateAdapter {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @FromJson
    fun fromJSON(date: String): LocalDate = LocalDate.parse(date, formatter)

    @ToJson
    fun toJSON(date: LocalDate): String = formatter.format(date)
}