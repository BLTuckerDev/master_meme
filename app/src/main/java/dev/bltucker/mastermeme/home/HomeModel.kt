package dev.bltucker.mastermeme.home

import dev.bltucker.mastermeme.common.room.MemeEntity
import dev.bltucker.mastermeme.common.templates.MemeTemplate
import dev.bltucker.mastermeme.home.composables.SortMode

data class HomeModel(
    val memes: List<MemeEntity> = emptyList(),
    val favoriteMemes: List<MemeEntity> = emptyList(),
    val memeSearchQuery: String = "",
    val memeTemplateSearchQuery: String ="",
    val sortMode: SortMode = SortMode.FAVORITES,
    val selectedMemes: List<MemeEntity> = emptyList(),
    val showTemplateSheet: Boolean = false,
    val showMemeTemplateSearch: Boolean = false,
    val memeTemplates: List<MemeTemplate> = emptyList(),
    val selectedMemeTemplate: MemeTemplate? = null,
    val isInSelectionMode: Boolean = false,
){
    val filteredMemes: List<MemeEntity> = when(sortMode){
        SortMode.FAVORITES -> favoriteMemes
        SortMode.NEWEST -> memes
    }
}