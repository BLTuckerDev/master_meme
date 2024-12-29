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
import dev.bltucker.mastermeme.common.MemeShareController
import dev.bltucker.mastermeme.common.templates.MemeTemplatesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMemeViewModel @Inject constructor(
    private val memeRepository: MemeRepository,
    private val memeTemplateRepository: MemeTemplatesRepository,
    private val memeShareController: MemeShareController,
) : ViewModel() {

    private val mutableModel = MutableStateFlow(CreateMemeModel())
    val observableModel: StateFlow<CreateMemeModel> = mutableModel

    fun onStart(templateId: Int) {
        val template = memeTemplateRepository.getTemplateById(templateId)
        mutableModel.update {
            it.copy(memeTemplate = template)
        }
    }

    fun onUpdateSelectedTextBoxProperties(textUnit: TextUnit, fontFamily: FontFamily, color: Color) {
        val selectedId = mutableModel.value.selectedTextBox?.id ?: return
        val currentBox = mutableModel.value.textBoxes.find { it.id == selectedId } ?: return

        Log.d("EDIT_DEBUG", "Confirming changes with - textUnit: $textUnit, fontFamily: $fontFamily, color: $color")
        Log.d("EDIT_DEBUG", "Current box before update: $currentBox")
        Log.d("EDIT_DEBUG", "Current temporary box: ${mutableModel.value.temporaryTextBox}")


        val baseBox = mutableModel.value.temporaryTextBox ?: currentBox

        Log.d("EDIT_DEBUG", "Using base box for update: $baseBox")

        val updatedTextBox = baseBox.copy(
            fontSize = textUnit,
            fontFamily = fontFamily,
            color = color
        )

        Log.d("EDIT_DEBUG", "Created updated box: $updatedTextBox")

        addAction(MemeAction.UpdateTextBoxProperties(
            textBox = currentBox,
            oldText = currentBox.text,
            newText = updatedTextBox.text,
            oldFontSize = currentBox.fontSize,
            oldFontFamily = currentBox.fontFamily,
            oldColor = currentBox.color,
            newFontSize = textUnit,
            newFontFamily = fontFamily,
            newColor = color
        ))

        mutableModel.update {
            it.copy(
                textBoxes = it.textBoxes.map { box ->
                    if (box.id == selectedId) updatedTextBox else box
                },
                selectedTextBox = null,
                temporaryTextBox = null,
                selectedTextEditOption = TextEditOption.NONE
            )
        }

        Log.d("EDIT_DEBUG", "Final state - text boxes: ${mutableModel.value.textBoxes}")

    }

    fun onAddTextBox(memeTextBox: MemeTextBox) {
        addAction(MemeAction.AddTextBox(memeTextBox))

        mutableModel.update {
            it.copy(
                textBoxes = it.textBoxes + memeTextBox,
            )
        }
    }

    fun onTextBoxSelected(textBox: MemeTextBox) {
        val currentBox = mutableModel.value.textBoxes.find { it.id == textBox.id } ?: return
        mutableModel.update {
            it.copy(
                selectedTextBox = currentBox,
                temporaryTextBox = null
            )
        }
    }

    fun onTemporaryTextBoxPropertiesUpdate(fontSize: TextUnit? = null, fontFamily: FontFamily? = null, color: Color? = null) {
        val selectedId = mutableModel.value.selectedTextBox?.id ?: return
        val currentBox = mutableModel.value.textBoxes.find { it.id == selectedId } ?: return

        Log.d("EDIT_DEBUG", "Temporary update - fontSize: $fontSize, fontFamily: $fontFamily, color: $color")
        Log.d("EDIT_DEBUG", "Current box before temp update: $currentBox")
        Log.d("EDIT_DEBUG", "Current temporary box before update: ${mutableModel.value.temporaryTextBox}")


        val baseBox = mutableModel.value.temporaryTextBox ?: currentBox

        val updatedTextBox = baseBox.copy(
            fontSize = fontSize ?: baseBox.fontSize,
            fontFamily = fontFamily ?: baseBox.fontFamily,
            color = color ?: baseBox.color
        )

        Log.d("EDIT_DEBUG", "Updated temporary box: $updatedTextBox")

        mutableModel.update {
            it.copy(temporaryTextBox = updatedTextBox)
        }
        Log.d("EDIT_DEBUG", "Model after temp update - temporary box: ${mutableModel.value.temporaryTextBox}")

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
        val oldPosition = textBox.position
        Log.d("POSITION_DEBUG", "Moving - Old position: $oldPosition")
        Log.d("POSITION_DEBUG", "Moving - New position: $newPosition")
        val updatedTextBox = textBox.copy(position = newPosition)
        Log.d("POSITION_DEBUG", "Moving - Updated box position: ${updatedTextBox.position}")

        addAction(MemeAction.MoveTextBox(textBox, oldPosition, newPosition))

        val currentTextBoxes = mutableModel.value.textBoxes
        val updatedTextBoxes = currentTextBoxes.map { box ->
            if (box.id == textBox.id) updatedTextBox else box
        }
        mutableModel.update {
            it.copy(
                textBoxes = updatedTextBoxes
            )
        }
        Log.d("POSITION_DEBUG", "Moving - Final state position: ${
            mutableModel.value.textBoxes.find { it.id == textBox.id }?.position
        }")
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

    fun onUndo() {
        val model = mutableModel.value
        if (model.currentActionIndex < 0) return
        val actionToUndo = model.lastActions[model.currentActionIndex]

        when (actionToUndo) {
            is MemeAction.AddTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes - actionToUndo.textBox,
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

            is MemeAction.UpdateTextBoxProperties -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes.map { box ->
                            if (box.id == actionToUndo.textBox.id) {
                                actionToUndo.textBox.copy(
                                    text = actionToUndo.oldText,
                                    fontSize = actionToUndo.oldFontSize,
                                    fontFamily = actionToUndo.oldFontFamily,
                                    color = actionToUndo.oldColor
                                )
                            } else box
                        },
                        currentActionIndex = it.currentActionIndex - 1
                    )
                }
            }

            is MemeAction.MoveTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes.map { box ->
                            if (box.id == actionToUndo.textBox.id) {
                                actionToUndo.textBox.copy(position = actionToUndo.oldPosition)
                            } else box
                        },
                        currentActionIndex = it.currentActionIndex - 1
                    )
                }
            }
        }
    }

    fun onRedo() {
        val model = mutableModel.value
        if (model.currentActionIndex >= model.lastActions.size - 1) return
        val actionToRedo = model.lastActions[model.currentActionIndex + 1]

        when (actionToRedo) {
            is MemeAction.AddTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes + actionToRedo.textBox,
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

            is MemeAction.UpdateTextBoxProperties -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes.map { box ->
                            if (box.id == actionToRedo.textBox.id) {
                                actionToRedo.textBox.copy(
                                    text = actionToRedo.newText,  // Use the new text value here
                                    fontSize = actionToRedo.newFontSize,
                                    fontFamily = actionToRedo.newFontFamily,
                                    color = actionToRedo.newColor
                                )
                            } else box
                        },
                        currentActionIndex = it.currentActionIndex + 1
                    )
                }
            }

            is MemeAction.MoveTextBox -> {
                mutableModel.update {
                    it.copy(
                        textBoxes = it.textBoxes.map { box ->
                            if (box.id == actionToRedo.textBox.id) {
                                actionToRedo.textBox.copy(position = actionToRedo.newPosition)
                            } else box
                        },
                        currentActionIndex = it.currentActionIndex + 1
                    )
                }
            }
        }
    }

    private fun addAction(action: MemeAction) {
        Log.d("ACTION_DEBUG", "Adding action: $action")
        Log.d("ACTION_DEBUG", "Current actions: ${mutableModel.value.lastActions}")
        mutableModel.update {
            val newActions = it.lastActions
                .take(it.currentActionIndex + 1)
                .toMutableList()
                .apply { add(action) }
                .takeLast(5)

            Log.d("ACTION_DEBUG", "New actions list: $newActions")

            it.copy(
                lastActions = newActions,
                currentActionIndex = newActions.size - 1
            )
        }

        Log.d("ACTION_DEBUG", "After update - actions: ${mutableModel.value.lastActions}")
        Log.d("ACTION_DEBUG", "After update - current index: ${mutableModel.value.currentActionIndex}")
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
                    mutableModel.update {
                        it.copy(saveCompleted = true)
                    }
                } catch (ex: Exception){
                    Log.d("SAVE_MEME", "Error Saving Meme: $ex")
                }
            }
        }
    }

    fun onShowEditMemeTextDialog(memeTextBox: MemeTextBox?) {
        mutableModel.update {
            it.copy(showEditMemeTextDialog = true, selectedTextBox = memeTextBox)
        }
    }

    fun onHideEditMemeTextDialog(){
        mutableModel.update {
            it.copy(showEditMemeTextDialog = false)
        }
    }

    fun onTextEditOptionSelected(option: TextEditOption) {
        Log.d("EDIT_DEBUG", "Selected edit option: $option")
        Log.d("EDIT_DEBUG", "Current selected box: ${mutableModel.value.selectedTextBox}")
        Log.d("EDIT_DEBUG", "Current temporary box: ${mutableModel.value.temporaryTextBox}")

        mutableModel.update {
            it.copy(selectedTextEditOption = option)
        }
    }

    fun onTextBoxUpdated(updatedTextBox: MemeTextBox) {
        val selectedId = mutableModel.value.selectedTextBox?.id ?: return
        val currentBox = mutableModel.value.textBoxes.find { it.id == selectedId } ?: return

        addAction(MemeAction.UpdateTextBoxProperties(
            textBox = currentBox,
            oldText = currentBox.text,
            newText = updatedTextBox.text,
            oldFontSize = currentBox.fontSize,
            oldFontFamily = currentBox.fontFamily,
            oldColor = currentBox.color,
            newFontSize = currentBox.fontSize,
            newFontFamily = currentBox.fontFamily,
            newColor = currentBox.color
        ))

        mutableModel.update { model ->
            model.copy(
                textBoxes = model.textBoxes.map { textBox ->
                    if (textBox.id == selectedId) {
                        currentBox.copy(text = updatedTextBox.text)
                    } else textBox
                },
                selectedTextBox = null,
                showEditMemeTextDialog = false
            )
        }
    }

    fun onShareMeme(bitmap: Bitmap) {
        viewModelScope.launch {
            mutableModel.value.memeTemplate?.let { template ->
                try{
                    val savedMeme = memeRepository.saveMeme(template.name, bitmap)
                    memeShareController.shareMemes(listOf(savedMeme))
                } catch (ex: Exception){
                    Log.d("SAVE_MEME", "Error Saving Meme: $ex")
                }
            }
        }
    }
}