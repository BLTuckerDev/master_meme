package dev.bltucker.mastermeme.creatememe

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.SystemFontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.bltucker.mastermeme.common.templates.MemeTemplate

data class CreateMemeModel(
    val memeTemplate: MemeTemplate? = null,
    val textBoxes: List<MemeTextBox> = emptyList(),
    val selectedTextBox: MemeTextBox? = null,
    val showSaveOptions: Boolean = false,
    val showExitDialog: Boolean = false,
    val showEditMemeTextDialog: Boolean = false,
    val lastActions: List<MemeAction> = emptyList(),
    val currentActionIndex: Int = -1
)

data class MemeTextBox(
    val id: String,
    val text: String,
    val position: Offset,
    val fontSize: TextUnit = 24.sp,
    val fontFamily: SystemFontFamily = FontFamily.Default,
    val color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White
)

sealed class MemeAction {
    data class AddTextBox(val textBox: MemeTextBox) : MemeAction()
    data class UpdateTextBox(val oldTextBox: MemeTextBox, val newTextBox: MemeTextBox) : MemeAction()
    data class DeleteTextBox(val textBox: MemeTextBox) : MemeAction()
}