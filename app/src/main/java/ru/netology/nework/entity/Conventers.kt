package ru.netology.nework.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nework.dto.Coordinates
import ru.netology.nework.dto.ListIds

private val gson = Gson()

class Conventers {

    @TypeConverter
    fun fromSet(value: Set<Long>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toSet(data: String): Set<Long> {
        return if (data.isBlank()) emptySet() else data.split(",").map(String::toLong).toSet()
    }

    @TypeConverter
    fun coordinatesToJson(coordinates: Coordinates?): String? {
        return if (coordinates == null) {
            null
        } else {
            gson.toJson(coordinates)
        }
    }

    @TypeConverter
    fun jsonToCoordinates(json: String?): Coordinates? {
        return if (json.isNullOrEmpty()) {
            null
        } else {
            val type = object : TypeToken<Coordinates>() {}.type
            gson.fromJson(json, type)
        }
    }

    @TypeConverter
    fun fromListIds(listIds: ListIds): String {
        return Gson().toJson(listIds)
    }

    @TypeConverter
    fun toListIds(sh: String): ListIds {
        return Gson().fromJson(sh, ListIds::class.java)
    }
}