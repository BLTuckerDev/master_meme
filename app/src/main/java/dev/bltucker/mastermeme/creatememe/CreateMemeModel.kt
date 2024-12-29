package dev.bltucker.mastermeme.creatememe

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.SystemFontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.bltucker.mastermeme.common.templates.MemeTemplate

data class CreateMemeModel(
    val memeTemplate: MemeTemplate? = null,

    val textBoxes: List<MemeTextBox> = emptyList(),
    val selectedTextBox: MemeTextBox? = null,
    val temporaryTextBox: MemeTextBox? = null,

    val showSaveOptions: Boolean = false,
    val showExitDialog: Boolean = false,
    val showEditMemeTextDialog: Boolean = false,

    val selectedTextEditOption: TextEditOption = TextEditOption.NONE,

    val lastActions: List<MemeAction> = emptyList(),
    val currentActionIndex: Int = -1,
    val saveCompleted: Boolean = false
){

    val showEditTextBar: Boolean = selectedTextBox != null

    fun getDisplayedTextBox(id: String): MemeTextBox? {
        return when {
            temporaryTextBox?.id == id -> temporaryTextBox
            else -> textBoxes.find { it.id == id }
        }
    }
}

data class MemeTextBox(
    val id: String,
    val text: String,
    val position: Offset,
    val fontSize: TextUnit = 48.sp,
    val fontFamily: FontFamily = FontFamily.Default,
    val color: Color = Color.White
)

sealed class MemeAction {
    data class AddTextBox(val textBox: MemeTextBox) : MemeAction()
    data class DeleteTextBox(val textBox: MemeTextBox) : MemeAction()
    data class MoveTextBox(val textBox: MemeTextBox, val oldPosition: Offset, val newPosition: Offset) : MemeAction()
    data class UpdateTextBoxProperties(
        val textBox: MemeTextBox,
        val oldText: String,
        val newText: String,
        val oldFontSize: TextUnit,
        val oldFontFamily: FontFamily,
        val oldColor: Color,
        val newFontSize: TextUnit,
        val newFontFamily: FontFamily,
        val newColor: Color
    ) : MemeAction()
}

enum class TextEditOption {
    FONT,
    SIZE,
    COLOR,
    NONE
}