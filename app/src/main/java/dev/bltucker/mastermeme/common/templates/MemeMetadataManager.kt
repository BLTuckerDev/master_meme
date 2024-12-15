package dev.bltucker.mastermeme.common.templates

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

data class MemeMetadata(
    val friendlyName: String,
    val searchTerms: Set<String> = emptySet()
)

@Singleton
class MemeMetadataManager @Inject constructor(@Named("meme_metadata_map") private val metadata: Map<String, MemeMetadata>) {

    fun getDisplayName(filename: String): String {
        return getMetadataForTemplate(filename)?.friendlyName ?: generateDefaultName(filename)
    }

    fun getSearchTerms(filename: String): Set<String> {
        return getMetadataForTemplate(filename)?.searchTerms ?: generateDefaultSearchTerms(filename)
    }

    private fun getMetadataForTemplate(filename: String): MemeMetadata? {
        val key = filename
            .removePrefix("meme_template_")
            .substringBeforeLast(".")
        return metadata[key]
    }

    private fun generateDefaultName(filename: String): String {
        return filename
            .removePrefix("meme_template_")
            .substringBeforeLast(".")
            .replace("_", " ")
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }
    }

    private fun generateDefaultSearchTerms(filename: String): Set<String> {
        val baseName = filename
            .removePrefix("meme_template_")
            .substringBeforeLast(".")
        return baseName.split("_").toSet()
    }
}