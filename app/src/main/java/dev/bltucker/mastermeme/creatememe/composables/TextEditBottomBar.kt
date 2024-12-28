package dev.bltucker.mastermeme.creatememe.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bltucker.mastermeme.R
import dev.bltucker.mastermeme.common.theme.MasterMemeTheme
import dev.bltucker.mastermeme.common.theme.MemeFont

enum class TextEditOption {
    FONT,
    SIZE,
    COLOR,
    NONE
}



@Composable
fun TextEditBottomBar(
    modifier: Modifier = Modifier,
    currentlySelectedOption: TextEditOption = TextEditOption.NONE,
    currentFontSize: TextUnit,
    selectedFontFamily: FontFamily,
    selectedColor: Color,
    onOptionSelected: (TextEditOption) -> Unit,
    onFontSizeChanged: (Float) -> Unit,
    onFontSelected: (FontFamily) -> Unit,
    onColorSelected: (Color) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,

) {

    Column(modifier = modifier) {
        when (currentlySelectedOption) {
            TextEditOption.SIZE -> FontSizeSelector(
                currentSize = currentFontSize,
                onSizeChanged = onFontSizeChanged
            )
            TextEditOption.FONT -> FontSelector(
                selectedFont = selectedFontFamily,
                onFontSelected = onFontSelected
            )
            TextEditOption.COLOR -> ColorSelector(
                selectedColor = selectedColor,
                onColorSelected = onColorSelected
            )
            TextEditOption.NONE -> { /* Nothing to show */ }
        }

        Surface(
            tonalElevation = 3.dp,
            modifier = modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel text editing",
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onOptionSelected(TextEditOption.FONT) }) {
                        Icon(
                            painter = painterResource(R.drawable.text_style_icon),
                            contentDescription = "Change font",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = { onOptionSelected(TextEditOption.SIZE) }) {
                        Icon(
                            painter = painterResource(R.drawable.text_size_icon),
                            contentDescription = "Change font size",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = { onOptionSelected(TextEditOption.COLOR) }) {
                        Image(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(R.drawable.color_wheel),
                            contentDescription = "Change text color",
                        )
                    }
                }

                IconButton(onClick = onConfirm) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Confirm text changes",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FontSizeSelector(
    currentSize: TextUnit,
    onSizeChanged: (Float) -> Unit
) {
    Surface(
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${currentSize.value.toInt()}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
            Slider(
                modifier = Modifier.weight(1f),
                value = currentSize.value,
                onValueChange = onSizeChanged,
                valueRange = 12f..72f,
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                },
                track = {
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                    )
                }
            )
        }
    }
}
@Composable
private fun FontSelector(
    selectedFont: FontFamily,
    onFontSelected: (FontFamily) -> Unit
) {
    val fonts = listOf(
        FontFamily.Default,
        FontFamily.Serif,
        FontFamily.SansSerif,
        FontFamily.Monospace,
        FontFamily.Cursive,
        MemeFont
    )

    Surface(
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(fonts) { font ->
                Text(
                    text = "Aa",
                    fontFamily = font,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onFontSelected(font) }
                        .background(
                            if (font == selectedFont) MaterialTheme.colorScheme.primaryContainer
                            else Color.Transparent
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

private fun getFontName(font: FontFamily): String {
    return when(font) {
        FontFamily.Default -> "Default"
        FontFamily.SansSerif -> "Sans Serif"
        FontFamily.Serif -> "Serif"
        FontFamily.Monospace -> "Monospace"
        else -> "Custom"
    }
}
@Composable
private fun ColorSelector(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    val colors = listOf(
        Color.White,
        Color.Red,
        Color.Yellow,
        Color.Green,
        Color.Cyan,
        Color.Blue,
        Color.Magenta,
        Color.Black
    )

    Surface(
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(colors) { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (color == selectedColor) 2.dp else 1.dp,
                            color = if (color == selectedColor)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                        .clickable { onColorSelected(color) }
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun TextEditBottomBarPreview_NoEdit() {
    MasterMemeTheme {
        TextEditBottomBar(
            onConfirm = {},
            onCancel = {},
            currentFontSize = 16.sp,
            selectedFontFamily = FontFamily.Default,
            selectedColor = Color.Black,
            onFontSizeChanged = {},
            onFontSelected = {},
            onColorSelected = {},
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextEditBottomBarPreview_FontSize() {
    MasterMemeTheme {
        TextEditBottomBar(
            onConfirm = {},
            onCancel = {},
            currentlySelectedOption = TextEditOption.SIZE,
            currentFontSize = 16.sp,
            selectedFontFamily = FontFamily.Default,
            selectedColor = Color.Black,
            onFontSizeChanged = {},
            onFontSelected = {},
            onColorSelected = {},
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextEditBottomBarPreview_FontSelect() {
    MasterMemeTheme {
        TextEditBottomBar(
            onConfirm = {},
            onCancel = {},
            currentlySelectedOption = TextEditOption.FONT,
            currentFontSize = 16.sp,
            selectedFontFamily = FontFamily.Default,
            selectedColor = Color.Black,
            onFontSizeChanged = {},
            onFontSelected = {},
            onColorSelected = {},
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextEditBottomBarPreview_ColorSelect() {
    MasterMemeTheme {
        TextEditBottomBar(
            onConfirm = {},
            onCancel = {},
            currentlySelectedOption = TextEditOption.COLOR,
            currentFontSize = 16.sp,
            selectedFontFamily = FontFamily.Default,
            selectedColor = Color.Black,
            onFontSizeChanged = {},
            onFontSelected = {},
            onColorSelected = {},
            onOptionSelected = {}
        )
    }
}