package dev.bltucker.mastermeme.common.templates

class MemeTemplateInitializer(
    private val memeMetadataManager: MemeMetadataManager,
    private val templateResourceIds: Map<Int, String>) {

    fun createTemplateList(): List<MemeTemplate> {
        return try{
            templateResourceIds.map { (resId, filename) ->
                MemeTemplate(
                    resourceId = resId,
                    name = generateNameFromFilename(filename),
                    searchTerms = generateSearchTerms(filename)
                )
            }
        } catch(ex: Exception){
            emptyList()
        }
    }

    private fun generateNameFromFilename(filename: String): String {
        return memeMetadataManager.getDisplayName(filename)
    }

    private fun generateSearchTerms(filename: String): Set<String> {
        return memeMetadataManager.getSearchTerms(filename)
    }
}