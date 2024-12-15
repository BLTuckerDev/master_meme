package dev.bltucker.mastermeme.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bltucker.mastermeme.common.room.MemeDao
import dev.bltucker.mastermeme.common.room.MemeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemeRepository @Inject constructor(
    private val memeDao: MemeDao,
    @ApplicationContext private val context: Context
) {
    //TODO inject
    private val internalStorageDir = context.filesDir

    suspend fun saveMeme(
        templateName: String,
        bitmap: Bitmap
    ): MemeEntity {
        val memeId = UUID.randomUUID().toString()
        val filename = "meme_$memeId.jpg"
        val file = File(internalStorageDir, filename)

        //TODO inject dispatchers
        withContext(Dispatchers.IO) {
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
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

    fun getMemeContentUri(memeEntity: MemeEntity): Uri {
        //TODO inject file provider authority
        val file = File(memeEntity.filepath)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    suspend fun loadMemeBitmap(memeEntity: MemeEntity): Bitmap? {
        //TODO inject dispatchers
        return withContext(Dispatchers.IO) {
            try {
                BitmapFactory.decodeFile(memeEntity.filepath)
            } catch (e: Exception) {
                null
            }
        }
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
}