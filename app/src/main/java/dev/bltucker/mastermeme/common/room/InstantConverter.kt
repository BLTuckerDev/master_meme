package dev.bltucker.mastermeme.common.room

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.Instant


@TypeConverters
class Converters {
    @TypeConverter
    fun instantToLong(instant: Instant): Long = instant.toEpochMilli()

    @TypeConverter
    fun longToInstant(epochMillis: Long): Instant = Instant.ofEpochMilli(epochMillis)
}