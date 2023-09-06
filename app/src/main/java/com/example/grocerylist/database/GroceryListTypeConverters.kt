package com.example.grocerylist.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.util.*

class GroceryListTypeConverters {
    private val gson: Gson = Gson()

    @TypeConverter
    fun asUuid(bytes: ByteArray): UUID {
        val bb: ByteBuffer = ByteBuffer.wrap(bytes)
        val firstLong: Long = bb.getLong()
        val secondLong: Long = bb.getLong()
        return UUID(firstLong, secondLong)
    }

    @TypeConverter
    fun asBytes(uuid: UUID): ByteArray {
        val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
        bb.putLong(uuid.getMostSignificantBits())
        bb.putLong(uuid.getLeastSignificantBits())
        return bb.array()
    }

    @TypeConverter
    fun asString(date: LocalDateTime): String {
        return date.toString()
    }

    @TypeConverter
    fun asLocalDateTime(date: String): LocalDateTime {
        return LocalDateTime.parse(date)
    }
}