package dev.bltucker.mastermeme.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bltucker.mastermeme.common.MemeRepository
import dev.bltucker.mastermeme.common.MemeShareController
import dev.bltucker.mastermeme.common.room.MemeEntity
import dev.bltucker.mastermeme.common.templates.MemeTemplate
import dev.bltucker.mastermeme.common.templates.MemeTemplatesRepository
import dev.bltucker.mastermeme.home.composables.SortMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val memeShareController: MemeShareController,
    private val memeTemplatesRepository: MemeTemplatesRepository,
    private val memeRepository: MemeRepository
) : ViewModel() {

    private val mutableModel: MutableStateFlow<HomeModel> = MutableStateFlow(HomeModel())

    val observableModel: StateFlow<HomeModel> = mutableModel.asStateFlow()

    private var hasStarted = false

    fun onStart(){
        if(hasStarted){
            return
        }

        hasStarted = true

        loadTemplates()
        observeMemes()
    }

    private fun loadTemplates(){
        val templates = memeTemplatesRepository.getAvailableTemplates()
        mutableModel.update {
            it.copy(memeTemplates = templates)
        }
    }

    private fun observeMemes(){
        viewModelScope.launch{
            memeRepository.getMemes(true).collect{ memes ->
                mutableModel.update {
                    it.copy(favoriteMemes = memes)
                }
            }
        }

        viewModelScope.launch{
            memeRepository.getMemes(false).collect{ memes ->
                mutableModel.update {
                    it.copy(memes = memes)
                }
            }
        }
    }

    fun onUpdateSortMode(sortMode: SortMode){
        mutableModel.update {
            it.copy(sortMode = sortMode)
        }
    }

    fun onSetBottomSheetVisibility(isVisible: Boolean){
        mutableModel.update {
            it.copy(showTemplateSheet = isVisible)
        }
    }

    fun onUpdateTemplateSearchQuery(query: String){
        mutableModel.update {
            it.copy(memeTemplateSearchQuery = query)
        }
    }

    fun onSetTemplateSearchVisibility(isVisible: Boolean){
        mutableModel.update {
            it.copy(showMemeTemplateSearch = isVisible)
        }
    }

    fun onTemplateSelected(memeTemplate: MemeTemplate?){
        mutableModel.update {
            it.copy(selectedMemeTemplate = memeTemplate, showTemplateSheet = false, showMemeTemplateSearch = false, memeTemplateSearchQuery = "")
        }
    }

    fun onExecuteTemplateSearch() {
        val searchQuery = observableModel.value.memeTemplateSearchQuery

        if(searchQuery.isBlank()){
            loadTemplates()
        } else {
            val templates = memeTemplatesRepository.searchTemplates(observableModel.value.memeTemplateSearchQuery)
            mutableModel.update {
                it.copy(memeTemplates = templates)
            }
        }
    }

    fun onLongClickMeme(longClickedMeme: MemeEntity){
        mutableModel.update {
            it.copy(selectedMemes = it.selectedMemes + longClickedMeme, isInSelectionMode = true)
        }
    }

    fun onToggleMemeFavoriteStatus(meme: MemeEntity){
        viewModelScope.launch {
            memeRepository.toggleFavorite(meme)
        }
    }

    fun onToggleSelected(memeEntity: MemeEntity) {
        viewModelScope.launch {
            if(mutableModel.value.selectedMemes.contains(memeEntity)) {
                mutableModel.update {
                    it.copy(selectedMemes = it.selectedMemes - memeEntity)
                }
            } else {
                mutableModel.update {
                    it.copy(selectedMemes = it.selectedMemes + memeEntity)
                }
            }
        }
    }

    fun onExitSelectionMode() {
        mutableModel.update {
            it.copy(isInSelectionMode = false, selectedMemes = emptyList())
        }
    }

    fun onShareSelectedMemes() {
        val selectedMemes = mutableModel.value.selectedMemes
        if(selectedMemes.isEmpty()){
            return
        }

        viewModelScope.launch {
            memeShareController.shareMemes(selectedMemes)
        }
    }

    fun onDeleteSelectedMemes() {
        viewModelScope.launch {
            val selectedMemes = mutableModel.value.selectedMemes
            selectedMemes.forEach { meme ->
                memeRepository.deleteMeme(meme)
            }
            mutableModel.update {
                it.copy(isInSelectionMode = false, selectedMemes = emptyList())
            }
        }
    }


}