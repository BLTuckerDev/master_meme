package dev.bltucker.mastermeme.common.templates

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class MemeTemplate(
    @DrawableRes val resourceId: Int,
    val name: String,
    val searchTerms: Set<String> = emptySet()
)

@Singleton
class MemeTemplatesRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val templateList = mutableListOf<MemeTemplate>()

    fun loadTemplates(templates: List<MemeTemplate>) {
        templateList.clear()
        templateList.addAll(templates)
    }

    fun getAvailableTemplates(): List<MemeTemplate> = templateList

    fun searchTemplates(query: String): List<MemeTemplate>{
        if (query.isBlank()) return templateList

        val searchQuery = query.lowercase().trim()
        return templateList.filter { template ->
            template.name.lowercase().contains(searchQuery) ||
                    template.searchTerms.any { it.lowercase().contains(searchQuery) }
        }
    }

    suspend fun getTemplateBitmap(template: MemeTemplate): Bitmap? =
        //TODO inject dispatchers
        withContext(Dispatchers.IO) {
            try {
                BitmapFactory.decodeResource(context.resources, template.resourceId)
            } catch (e: Exception) {
                null
            }
        }

    fun getTemplateById(templateId: Int): MemeTemplate? {
        return templateList.firstOrNull() { it.resourceId == templateId }
    }
}

