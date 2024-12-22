package dev.bltucker.mastermeme.home

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bltucker.mastermeme.common.templates.MemeTemplatesRepository
import dev.bltucker.mastermeme.home.composables.SortMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val memeTemplatesRepository: MemeTemplatesRepository) : ViewModel() {

    private val mutableModel: MutableStateFlow<HomeModel> = MutableStateFlow(HomeModel())

    val observableModel: StateFlow<HomeModel> = mutableModel.asStateFlow()

    private var hasStarted = false

    fun onStart(){
        if(hasStarted){
            return
        }

        hasStarted = true

        loadTemplates()
    }

    private fun loadTemplates(){
        val templates = memeTemplatesRepository.getAvailableTemplates()
        Log.d("HomeViewModel", "Templates count: ${templates.size}")
        mutableModel.update {
            it.copy(memeTemplates = templates)
        }
    }


    fun onUpdateSortMode(sortMode: SortMode){
        mutableModel.update {
            it.copy(sortMode = sortMode)
        }
    }

    fun onDismissTemplateSheet() {
        mutableModel.update {
            it.copy(showTemplateSheet = false)
        }
    }

    fun onShowTemplateSheet() {
        mutableModel.update {
            it.copy(showTemplateSheet = true)
        }
    }
}