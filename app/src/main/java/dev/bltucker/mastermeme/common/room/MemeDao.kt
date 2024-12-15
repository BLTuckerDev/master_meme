package dev.bltucker.mastermeme.common.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemeDao {
    @Query("SELECT * FROM memes ORDER BY CASE WHEN isFavorite = 1 THEN 0 ELSE 1 END, createdDate DESC")
        fun getMemesFavoritesFirst(): Flow<List<MemeEntity>>

        @Query("SELECT * FROM memes ORDER BY createdDate DESC")
        fun getMemesNewestFirst(): Flow<List<MemeEntity>>

        @Insert
        suspend fun insertMeme(meme: MemeEntity)

        @Update
        suspend fun updateMeme(meme: MemeEntity)

        @Delete
        suspend fun deleteMeme(meme: MemeEntity)
}