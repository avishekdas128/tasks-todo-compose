package com.orangeink.taskstodocompose.data.db.converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let {
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).parse(it)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.let {
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(it)
        }
    }
}