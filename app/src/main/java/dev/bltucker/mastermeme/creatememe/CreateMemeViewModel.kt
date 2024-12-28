package dev.bltucker.mastermeme.creatememe

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
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

    fun onUpdateSelectedTextBox(textUnit: TextUnit, fontFamily: FontFamily, color: Color) {
        val selectedTextBox = mutableModel.value.selectedTextBox ?: return
        val updatedTextBox = selectedTextBox.copy(
            fontSize = textUnit,
            fontFamily = fontFamily,
            color = color
        )

        updateTextBox(selectedTextBox, updatedTextBox)

        mutableModel.update {
            it.copy(selectedTextBox = null)
        }
    }

    fun onAddTextBox(memeTextBox: MemeTextBox) {
        addAction(MemeAction.AddTextBox(memeTextBox))

        mutableModel.update {
            it.copy(
                textBoxes = it.textBoxes + memeTextBox,
                selectedTextBox = memeTextBox
            )
        }
    }

    fun onTextBoxSelected(textBox: MemeTextBox) {
        mutableModel.update {
            it.copy(
                selectedTextBox = textBox,
                temporaryTextBox = null
            )
        }
    }

    fun onTemporaryTextBoxUpdate(fontSize: TextUnit? = null, fontFamily: FontFamily? = null, color: Color? = null) {
        val currentTextBox = mutableModel.value.selectedTextBox ?: return
        val currentTemp = mutableModel.value.temporaryTextBox ?: currentTextBox

        val updatedTextBox = currentTemp.copy(
            fontSize = fontSize ?: currentTemp.fontSize,
            fontFamily = fontFamily ?: currentTemp.fontFamily,
            color = color ?: currentTemp.color
        )

        mutableModel.update {
            it.copy(temporaryTextBox = updatedTextBox)
        }
    }

    fun onConfirmTextBoxChanges() {
        val selectedTextBox = mutableModel.value.selectedTextBox ?: return
        val temporaryTextBox = mutableModel.value.temporaryTextBox ?: return

        updateTextBox(selectedTextBox, temporaryTextBox)

        mutableModel.update {
            it.copy(
                selectedTextBox = null,
                temporaryTextBox = null,
                selectedTextEditOption = TextEditOption.NONE
            )
        }
    }

    fun onCancelTextBoxChanges() {
        mutableModel.update {
            it.copy(
                selectedTextBox = null,
                temporaryTextBox = null,
                selectedTextEditOption = TextEditOption.NONE
            )
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
                        currentActionIndex = it.currentActionIndex - 1,
                        selectedTextBox = null,
                        temporaryTextBox = null
                    )
                }
            }
            is MemeAction.UpdateTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes.map { box ->
                            if (box.id == actionToUndo.newTextBox.id) actionToUndo.oldTextBox else box
                        },
                        currentActionIndex = it.currentActionIndex - 1,
                        selectedTextBox = null,
                        temporaryTextBox = null
                    )
                }
            }
            is MemeAction.DeleteTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes + actionToUndo.textBox,
                        currentActionIndex = it.currentActionIndex - 1,
                        selectedTextBox = null,
                        temporaryTextBox = null
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
                        currentActionIndex = it.currentActionIndex + 1,
                        selectedTextBox = null,
                        temporaryTextBox = null
                    )
                }
            }
            is MemeAction.UpdateTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes.map { box ->
                            if (box.id == actionToRedo.oldTextBox.id) actionToRedo.newTextBox else box
                        },
                        currentActionIndex = it.currentActionIndex + 1,
                        selectedTextBox = null,
                        temporaryTextBox = null
                    )
                }
            }
            is MemeAction.DeleteTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes - actionToRedo.textBox,
                        currentActionIndex = it.currentActionIndex + 1,
                        selectedTextBox = null,
                        temporaryTextBox = null
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
                try{
                    memeRepository.saveMeme(template.name, bitmap)
                } catch (ex: Exception){
                    Log.d("SAVE_MEME", "Error Saving Meme: $ex")
                }
            }
        }
    }

    fun onShowEditMemeTextDialog() {
        mutableModel.update {
            it.copy(showEditMemeTextDialog = true)
        }
    }

    fun onHideEditMemeTextDialog(){
        mutableModel.update {
            it.copy(showEditMemeTextDialog = false)
        }
    }

    fun onTextEditOptionSelected(option: TextEditOption) {
        mutableModel.update {
            it.copy(selectedTextEditOption = option)
        }
    }
}