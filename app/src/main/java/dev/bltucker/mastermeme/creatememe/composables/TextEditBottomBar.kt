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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import dev.bltucker.mastermeme.creatememe.TextEditOption


@Composable
fun TextEditBottomBar(
    modifier: Modifier = Modifier,
    currentlySelectedOption: TextEditOption = TextEditOption.NONE,
    currentFontSize: TextUnit,
    currentFontFamily: FontFamily,
    currentColor: Color,
    onOptionSelected: (TextEditOption) -> Unit,
    onConfirmChanges: (TextUnit, FontFamily, Color) -> Unit,
    onCancel: () -> Unit,
    ) {

    var color by remember { mutableStateOf(currentColor) }
    var fontSize by remember { mutableStateOf(currentFontSize) }
    var fontFamily by remember { mutableStateOf(currentFontFamily) }

    Column(modifier = modifier) {
        when (currentlySelectedOption) {
            TextEditOption.SIZE -> FontSizeSelector(
                currentSize = currentFontSize,
                onSizeChanged = {
                    fontSize = it
                }
            )
            TextEditOption.FONT -> FontSelector(
                selectedFont = currentFontFamily,
                onFontSelected = {
                    fontFamily = it
                }
            )
            TextEditOption.COLOR -> ColorSelector(
                selectedColor = currentColor,
                onColorSelected = {
                    color = it
                }
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

                IconButton(onClick = {
                    onConfirmChanges(fontSize, fontFamily, color)
                }) {
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
    onSizeChanged: (TextUnit) -> Unit
) {

    var sliderValue by remember { mutableStateOf(currentSize.value) }

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
                text = "${sliderValue.toInt()}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
            Slider(
                modifier = Modifier.weight(1f),
                value = sliderValue,
                onValueChange = { newSize ->
                    sliderValue = newSize
                    onSizeChanged(TextUnit(newSize, currentSize.type))
                },
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

    var currentFont by remember { mutableStateOf(selectedFont) }

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
                        .clickable {
                            currentFont = font
                            onFontSelected(font)
                        }
                        .background(
                            if (font == currentFont) MaterialTheme.colorScheme.primaryContainer
                            else Color.Transparent
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
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

    var currentColor by remember { mutableStateOf(selectedColor) }

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
                            width = if (color == currentColor) 2.dp else 1.dp,
                            color = if (color == currentColor)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                        .clickable {
                            currentColor = color
                            onColorSelected(color)
                        }
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
            onConfirmChanges = { _, _, _ -> },
            onCancel = {},
            currentFontSize = 16.sp,
            currentFontFamily = FontFamily.Default,
            currentColor = Color.Black,
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextEditBottomBarPreview_FontSize() {
    MasterMemeTheme {
        TextEditBottomBar(
            onConfirmChanges = { _, _, _ -> },
            onCancel = {},
            currentlySelectedOption = TextEditOption.SIZE,
            currentFontSize = 16.sp,
            currentFontFamily = FontFamily.Default,
            currentColor = Color.Black,
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextEditBottomBarPreview_FontSelect() {
    MasterMemeTheme {
        TextEditBottomBar(
            onConfirmChanges = { _, _, _ -> },
            onCancel = {},
            currentlySelectedOption = TextEditOption.FONT,
            currentFontSize = 16.sp,
            currentFontFamily = FontFamily.Default,
            currentColor = Color.Black,
            onOptionSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextEditBottomBarPreview_ColorSelect() {
    MasterMemeTheme {
        TextEditBottomBar(
            onConfirmChanges = { _, _, _ -> },
            onCancel = {},
            currentlySelectedOption = TextEditOption.COLOR,
            currentFontSize = 16.sp,
            currentFontFamily = FontFamily.Default,
            currentColor = Color.Black,
            onOptionSelected = {_ ->}
        )
    }
}