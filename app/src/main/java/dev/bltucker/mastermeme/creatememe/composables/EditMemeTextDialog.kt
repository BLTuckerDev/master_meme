package dev.bltucker.mastermeme.creatememe.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bltucker.mastermeme.common.theme.MasterMemeTheme

@Composable
fun EditMemeTextDialog(
    modifier: Modifier = Modifier,
    text: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var editedText by remember { mutableStateOf(text) }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        shape = RoundedCornerShape(8.dp),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit text",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            TextField(
                modifier = Modifier.fillMaxWidth()
                    .focusRequester(focusRequester),
                value = editedText,
                onValueChange = { editedText = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                ),
                placeholder = { Text("Enter text") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(editedText)
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun EditMemeTextDialogPreview_Empty() {
    MasterMemeTheme {
        EditMemeTextDialog(
            text = "",
            onDismiss = {},
            onSave = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditMemeTextDialogPreview_WithText() {
    MasterMemeTheme {
        EditMemeTextDialog(
            text = "Sample meme text",
            onDismiss = {},
            onSave = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditMemeTextDialogPreview_LongText() {
    MasterMemeTheme {
        EditMemeTextDialog(
            text = "This is a really long piece of meme text that might need to wrap to multiple lines in the dialog",
            onDismiss = {},
            onSave = {}
        )
    }
}