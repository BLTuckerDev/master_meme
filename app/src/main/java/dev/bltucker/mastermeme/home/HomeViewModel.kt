package dev.bltucker.mastermeme.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bltucker.mastermeme.home.composables.SortMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val mutableModel: MutableStateFlow<HomeModel> = MutableStateFlow(HomeModel())

    val observableModel: StateFlow<HomeModel> = mutableModel.asStateFlow()

    private var hasStarted = false

    fun onStart(){
        if(hasStarted){
            return
        }

        hasStarted = true
    }



    fun onUpdateSortMode(sortMode: SortMode){
        mutableModel.update {
            it.copy(sortMode = sortMode)
        }
    }
}