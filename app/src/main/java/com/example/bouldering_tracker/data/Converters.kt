package com.example.bouldering_tracker.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromClimbList(value: MutableList<Climb>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toClimbList(value: String): MutableList<Climb> {
        val listType = object : TypeToken<MutableList<Climb>>() {}.type
        return Gson().fromJson(value, listType)
    }
}