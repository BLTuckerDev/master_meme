package dev.bltucker.mastermeme.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bltucker.mastermeme.common.room.MemeDao
import dev.bltucker.mastermeme.common.room.MemeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemeRepository @Inject constructor(
    private val memeDao: MemeDao,
    @ApplicationContext private val context: Context,
    private val mediaFileManager: MediaFileManager
) {

    suspend fun saveMeme(
        templateName: String,
        bitmap: Bitmap
    ): MemeEntity {
        val memeId = UUID.randomUUID().toString()
        val filename = "meme_$memeId.jpg"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Memes").apply {
            if (!exists()) {
                mkdirs()
            }
        }.let { memesDir ->
            File(memesDir, filename)
        }

        withContext(Dispatchers.IO) {
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            mediaFileManager.addFileToMediaStore(file)
        }

        val memeEntity = MemeEntity(
            id = memeId,
            templateName = templateName,
            createdDate = Instant.now(),
            filepath = file.absolutePath
        )

        memeDao.insertMeme(memeEntity)
        return memeEntity
    }

    suspend fun toggleFavorite(memeEntity: MemeEntity) {
        memeDao.updateMeme(memeEntity.copy(isFavorite = !memeEntity.isFavorite))
    }

    suspend fun deleteMeme(memeEntity: MemeEntity) {
        //TODO inject dispatchers
        withContext(Dispatchers.IO) {
            File(memeEntity.filepath).delete()
        }
        memeDao.deleteMeme(memeEntity)
    }

    fun getMemes(sortByFavorites: Boolean): Flow<List<MemeEntity>> {
        return if (sortByFavorites) {
            memeDao.getMemesFavoritesFirst()
        } else {
            memeDao.getMemesNewestFirst()
        }
    }

    private suspend fun validateMemeFile(meme: MemeEntity): Boolean {
        val file = File(meme.filepath)
        if (!file.exists()) {
            // File was deleted outside the app, clean up the database
            memeDao.deleteMeme(meme)
            return false
        }
        return true
    }

    suspend fun cleanupOrphanedEntries() {
        withContext(Dispatchers.IO) {
            val memes = memeDao.getMemesNewestFirst().firstOrNull()
            memes?.forEach { meme ->
                validateMemeFile(meme)
            }
        }
    }
}