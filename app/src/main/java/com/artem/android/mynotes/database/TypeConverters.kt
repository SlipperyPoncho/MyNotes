package com.artem.android.mynotes.database

import androidx.room.TypeConverter
import java.util.UUID

class TypeConverters {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
}