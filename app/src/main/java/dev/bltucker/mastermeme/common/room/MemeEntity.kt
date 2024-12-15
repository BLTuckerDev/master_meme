package dev.bltucker.mastermeme.common.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(tableName = "memes")
data class MemeEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val templateName: String,
    val createdDate: Instant,
    val isFavorite: Boolean = false,
    val filepath: String
)