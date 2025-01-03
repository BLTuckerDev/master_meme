package dev.bltucker.mastermeme.creatememe.composables

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bltucker.mastermeme.common.theme.MasterMemeTheme
import dev.bltucker.mastermeme.creatememe.MemeTextBox
import kotlin.math.roundToInt

@Composable
fun MemeTextOverlay(
    modifier: Modifier = Modifier,
    memeTextBox: MemeTextBox,
    onDoubleTap: (MemeTextBox) -> Unit,
    isSelected: Boolean,
    onDelete: () -> Unit,
    onTap: () -> Unit,
    parentBounds: () -> Size,
    onTextBoxMoved: (MemeTextBox, Offset) -> Unit,
) {
    var offsetX by remember(memeTextBox.position) { mutableFloatStateOf(memeTextBox.position.x) }
    var offsetY by remember(memeTextBox.position) { mutableFloatStateOf(memeTextBox.position.y) }
    var textSize by remember(memeTextBox.fontSize, memeTextBox.text) { mutableStateOf(Size.Zero) }

    val bounds = parentBounds()

    Box(
        modifier = modifier
            .offset {
                IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
            }
            .onSizeChanged { size ->
                textSize = Size(size.width.toFloat(), size.height.toFloat())
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        onTextBoxMoved(memeTextBox, Offset(offsetX, offsetY))
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()

                        val newX = (offsetX + dragAmount.x).coerceIn(
                            0f,
                            bounds.width - textSize.width
                        )
                        val newY = (offsetY + dragAmount.y).coerceIn(
                            0f,
                            bounds.height - textSize.height
                        )

                        offsetX = newX
                        offsetY = newY
                    }
                )
            },
    ) {
        Surface(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onTap()
                        },
                        onDoubleTap = {
                            onDoubleTap(memeTextBox)
                        }
                    )
                },
            color = Color.Transparent,
        ) {
            Box {
                Text(
                    text = memeTextBox.text,
                    color = memeTextBox.color,
                    fontSize = memeTextBox.fontSize,
                    fontFamily = memeTextBox.fontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .then(
                            if (isSelected) {
                                Modifier.border(
                                    width = 1.dp,
                                    color = Color.White,
                                    shape = MaterialTheme.shapes.small
                                )
                            } else {
                                Modifier
                            }
                        )
                        .padding(4.dp)
                )

                if (isSelected) {
                    Badge(
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.TopEnd)
                            .clickable(onClick = onDelete)
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Default.Close,
                            contentDescription = "Delete text box"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemeTextOverlayPreview_Selected() {
    MasterMemeTheme {
        Surface(modifier = Modifier.padding(10.dp)) {
            MemeTextOverlay(
                memeTextBox = MemeTextBox("id", "Sample Meme Text", position = Offset(0f, 0f), fontSize = 24.sp, fontFamily = FontFamily.Default, color = Color.White),
                isSelected = true,
                onDelete = {},
                onTap = {},
                parentBounds = { Size(100f, 100f) },
                onTextBoxMoved = { _, _ -> },
                onDoubleTap = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemeTextOverlayPreview_Unselected() {
    MasterMemeTheme {
        Surface {
            MemeTextOverlay(
                memeTextBox = MemeTextBox("id", "Sample Meme Text", position = Offset(0f, 0f), fontSize = 24.sp, fontFamily = FontFamily.Default, color = Color.White),
                isSelected = false,
                onDelete = {},
                onTap = {},
                parentBounds = { Size(100f, 100f) },
                onTextBoxMoved = { _, _ -> },
                onDoubleTap = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemeTextOverlayPreview_LongText() {
    MasterMemeTheme {
        Surface {
            MemeTextOverlay(
                memeTextBox = MemeTextBox("id",  "This is a longer piece of meme text that might need to wrap", position = Offset(0f, 0f), fontSize = 24.sp, fontFamily = FontFamily.Default, color = Color.White),
                isSelected = true,
                onDelete = {},
                onTap = {},
                parentBounds = { Size(100f, 100f) },
                onTextBoxMoved = { _, _ -> },
                onDoubleTap = {}
            )
        }
    }
}