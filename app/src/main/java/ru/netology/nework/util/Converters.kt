package ru.netology.nework.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTime(dateTime: String): String? {
        return if (dateTime !== "") {
            val parsedDate = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
            return parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        } else ""
    }
}