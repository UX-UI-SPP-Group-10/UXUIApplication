package com.group10.uxuiapp.data

import androidx.room.TypeConverter

class TagConverter {
    @TypeConverter
    fun fromTagString(value: String?): List <String> {
        return value?.split(",")?.map {it.trim() } ?: emptyList()
    }

    @TypeConverter
    fun toTagString(tags: List<String>): String {
        return tags.joinToString(",")
    }
}