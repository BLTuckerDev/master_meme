package dev.bltucker.mastermeme.creatememe.composables

import androidx.compose.material3.Badge

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bltucker.mastermeme.common.theme.MasterMemeTheme
import kotlin.math.roundToInt

@Composable
fun MemeTextOverlay(
    modifier: Modifier = Modifier,
    text: String,
    offset: Offset,
    fontSize: TextUnit,
    fontFamily: FontFamily,
    color: Color,
    isSelected: Boolean,
    onDelete: () -> Unit,
    onTap: () -> Unit,
    parentBounds: () -> Size
) {
    var offsetX by remember { mutableFloatStateOf(offset.x) }
    var offsetY by remember { mutableFloatStateOf(offset.y) }
    var textSize by remember { mutableStateOf(Size.Zero) }

    val bounds = parentBounds()

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .onSizeChanged { size ->
                textSize = Size(size.width.toFloat(), size.height.toFloat())
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    // Calculate new position
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
            },
    ) {
        Surface(
            onClick = onTap,
            color = Color.Transparent,
        ) {
            Box {
                Text(
                    text = text,
                    color = color,
                    fontSize = fontSize,
                    fontFamily = fontFamily,
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
                        .padding(horizontal = 24.dp, vertical = 16.dp)
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
                text = "Sample Meme Text",
                fontSize = 24.sp,
                fontFamily = FontFamily.Default,
                color = Color.White,
                isSelected = true,
                onDelete = {},
                onTap = {},
                offset = Offset(0f, 0f),
                parentBounds = { Size(100f, 100f) }
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
                text = "Sample Meme Text",
                fontSize = 24.sp,
                fontFamily = FontFamily.Default,
                color = Color.White,
                isSelected = false,
                onDelete = {},
                onTap = {},
                offset = Offset(0f, 0f),
                parentBounds = { Size(100f, 100f) }
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
                text = "This is a longer piece of meme text that might need to wrap",
                fontSize = 24.sp,
                fontFamily = FontFamily.Default,
                color = Color.White,
                isSelected = true,
                onDelete = {},
                onTap = {},
                offset = Offset(0f, 0f),
                parentBounds = { Size(100f, 100f) }
            )
        }
    }
}