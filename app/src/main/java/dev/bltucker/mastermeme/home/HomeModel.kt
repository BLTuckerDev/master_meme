package dev.bltucker.mastermeme.home

import dev.bltucker.mastermeme.common.room.MemeEntity
import dev.bltucker.mastermeme.home.composables.SortMode

data class HomeModel(
    val memes: List<MemeEntity> = emptyList(),
    val searchQuery: String = "",
    val sortMode: SortMode = SortMode.FAVORITES,
    val selectedMemes: List<MemeEntity> = emptyList(),
)