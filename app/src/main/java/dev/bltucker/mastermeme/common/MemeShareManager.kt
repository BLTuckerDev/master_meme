package dev.bltucker.mastermeme.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bltucker.mastermeme.common.room.MemeEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemeShareController @Inject constructor(@ApplicationContext private val context: Context) {
    fun shareMemes(memes: List<MemeEntity>) {
        if (memes.isEmpty()) return

        val uris = memes.mapNotNull { meme ->
            try {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    java.io.File(meme.filepath)
                )
            } catch (e: IllegalArgumentException) {
                // Log error or handle invalid file
                null
            }
        }

        if (uris.isEmpty()) return

        val shareIntent = createShareIntent(uris)

        // Grant temporary read permission to receiving apps
        uris.forEach { uri ->
            context.grantUriPermission(
                context.packageName,
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        // Start the share activity
        val chooserIntent = Intent.createChooser(shareIntent, "Share Meme${if (uris.size > 1) "s" else ""}")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    private fun createShareIntent(uris: List<Uri>): Intent {
        return if (uris.size == 1) {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uris.first())
                type = "image/*"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
        } else {
            Intent().apply {
                action = Intent.ACTION_SEND_MULTIPLE
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
                type = "image/*"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
        }
    }
}