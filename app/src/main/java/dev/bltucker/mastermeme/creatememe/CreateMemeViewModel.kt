package dev.bltucker.mastermeme.creatememe

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bltucker.mastermeme.common.MemeRepository
import dev.bltucker.mastermeme.common.templates.MemeTemplatesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateMemeViewModel @Inject constructor(
    private val memeRepository: MemeRepository,
    private val memeTemplateRepository: MemeTemplatesRepository,
) : ViewModel() {

    private val mutableModel = MutableStateFlow(CreateMemeModel())
    val observableModel: StateFlow<CreateMemeModel> = mutableModel

    fun onStart(templateId: Int) {
        val template = memeTemplateRepository.getTemplateById(templateId)
        mutableModel.update {
            it.copy(memeTemplate = template)
        }
    }

    fun onAddTextBox() {
        val newTextBox = MemeTextBox(
            id = UUID.randomUUID().toString(),
            text = "",
            position = Offset(0f, 0f)
        )

        addAction(MemeAction.AddTextBox(newTextBox))

        mutableModel.update {
            it.copy(
                textBoxes = it.textBoxes + newTextBox,
                selectedTextBox = newTextBox
            )
        }
    }

    fun onTextBoxSelected(textBox: MemeTextBox) {
        mutableModel.update {
            it.copy(selectedTextBox = textBox)
        }
    }

    fun onTextBoxMoved(textBox: MemeTextBox, newPosition: Offset) {
        val updatedTextBox = textBox.copy(position = newPosition)
        updateTextBox(textBox, updatedTextBox)
    }

    fun onTextBoxDeleted(textBox: MemeTextBox) {
        addAction(MemeAction.DeleteTextBox(textBox))

        mutableModel.update {
            it.copy(
                textBoxes = it.textBoxes - textBox,
                selectedTextBox = if (it.selectedTextBox == textBox) null else it.selectedTextBox
            )
        }
    }

    fun onUpdateTextBoxText(textBox: MemeTextBox, newText: String) {
        val updatedTextBox = textBox.copy(text = newText)
        updateTextBox(textBox, updatedTextBox)
    }

    fun onUpdateTextBoxFontSize(textBox: MemeTextBox, fontSize: Float) {
        val updatedTextBox = textBox.copy(fontSize = fontSize)
        updateTextBox(textBox, updatedTextBox)
    }

    private fun updateTextBox(oldTextBox: MemeTextBox, newTextBox: MemeTextBox) {
        addAction(MemeAction.UpdateTextBox(oldTextBox, newTextBox))

        mutableModel.update {
            it.copy(
                textBoxes = it.textBoxes.map { box ->
                    if (box.id == oldTextBox.id) newTextBox else box
                },
                selectedTextBox = if (it.selectedTextBox?.id == oldTextBox.id) newTextBox else it.selectedTextBox
            )
        }
    }

    fun onUndo() {
        val model = mutableModel.value
        if (model.currentActionIndex < 0) return

        when (val actionToUndo = model.lastActions[model.currentActionIndex]) {
            is MemeAction.AddTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes - actionToUndo.textBox,
                        currentActionIndex = it.currentActionIndex - 1
                    )
                }
            }
            is MemeAction.UpdateTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes.map { box ->
                            if (box.id == actionToUndo.newTextBox.id) actionToUndo.oldTextBox else box
                        },
                        currentActionIndex = it.currentActionIndex - 1
                    )
                }
            }
            is MemeAction.DeleteTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes + actionToUndo.textBox,
                        currentActionIndex = it.currentActionIndex - 1
                    )
                }
            }
        }
    }

    fun onRedo() {
        val model = mutableModel.value
        if (model.currentActionIndex >= model.lastActions.size - 1) return

        when (val actionToRedo = model.lastActions[model.currentActionIndex + 1]) {
            is MemeAction.AddTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes + actionToRedo.textBox,
                        currentActionIndex = it.currentActionIndex + 1
                    )
                }
            }
            is MemeAction.UpdateTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes.map { box ->
                            if (box.id == actionToRedo.oldTextBox.id) actionToRedo.newTextBox else box
                        },
                        currentActionIndex = it.currentActionIndex + 1
                    )
                }
            }
            is MemeAction.DeleteTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes - actionToRedo.textBox,
                        currentActionIndex = it.currentActionIndex + 1
                    )
                }
            }
        }
    }

    private fun addAction(action: MemeAction) {
        mutableModel.update {
            val newActions = it.lastActions
                .take(it.currentActionIndex + 1)
                .toMutableList()
                .apply { add(action) }
                .takeLast(5)

            it.copy(
                lastActions = newActions,
                currentActionIndex = newActions.size - 1
            )
        }
    }

    fun onToggleSaveOptions() {
        mutableModel.update {
            it.copy(showSaveOptions = !it.showSaveOptions)
        }
    }

    fun onToggleExitDialog() {
        mutableModel.update {
            it.copy(showExitDialog = !it.showExitDialog)
        }
    }

    fun onSaveMeme(bitmap: Bitmap) {
        viewModelScope.launch {
            mutableModel.value.memeTemplate?.let { template ->
                memeRepository.saveMeme(template.name, bitmap)
            }
        }
    }
}