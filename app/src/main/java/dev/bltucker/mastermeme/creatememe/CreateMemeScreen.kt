package dev.bltucker.mastermeme.creatememe

import ExitConfirmationDialog
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import dev.bltucker.mastermeme.creatememe.composables.CreateMemeBottomBar
import dev.bltucker.mastermeme.creatememe.composables.CreateMemeTopBar
import dev.bltucker.mastermeme.creatememe.composables.SaveOptionsSheet
import kotlinx.serialization.Serializable

@Serializable
data class CreateMemeTemplateArgs(val templateId: Int)

fun NavGraphBuilder.createMemeScreen(onBackPress: () -> Unit) {
    composable<CreateMemeTemplateArgs>{ backStackEntry ->
        val args = backStackEntry.toRoute<CreateMemeTemplateArgs>()
        val viewModel = hiltViewModel<CreateMemeViewModel>()
        val model by viewModel.observableModel.collectAsStateWithLifecycle()

        LifecycleStartEffect(Unit) {
            viewModel.onStart(args.templateId)

            onStopOrDispose {  }
        }

        CreateMemeScreen(
            modifier = Modifier.fillMaxSize(),
            model = model,
            onBackPress = onBackPress,
            onAddTextBox = viewModel::onAddTextBox,
            onTextBoxSelected = viewModel::onTextBoxSelected,
            onTextBoxMoved = viewModel::onTextBoxMoved,
            onTextBoxDeleted = viewModel::onTextBoxDeleted,
            onUpdateTextBoxText = viewModel::onUpdateTextBoxText,
            onUpdateTextBoxFontSize = viewModel::onUpdateTextBoxFontSize,
            onUndo = viewModel::onUndo,
            onRedo = viewModel::onRedo,
            onToggleSaveOptions = viewModel::onToggleSaveOptions,
            onToggleExitDialog = viewModel::onToggleExitDialog,
            onSaveMeme = viewModel::onSaveMeme
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMemeScreen(
    model: CreateMemeModel,
    onBackPress: () -> Unit,
    onAddTextBox: () -> Unit,
    onTextBoxSelected: (MemeTextBox) -> Unit,
    onTextBoxMoved: (MemeTextBox, Offset) -> Unit,
    onTextBoxDeleted: (MemeTextBox) -> Unit,
    onUpdateTextBoxText: (MemeTextBox, String) -> Unit,
    onUpdateTextBoxFontSize: (MemeTextBox, Float) -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onToggleSaveOptions: () -> Unit,
    onToggleExitDialog: () -> Unit,
    onSaveMeme: (Bitmap) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CreateMemeTopBar(
                onBackPressed = onToggleExitDialog
            )
        },
        bottomBar = {
            CreateMemeBottomBar(
                modifier = Modifier.navigationBarsPadding(),
                onAddTextBox = onAddTextBox,
                onUndo = onUndo,
                onRedo = onRedo,
                onSave = onToggleSaveOptions,
                canUndo = model.currentActionIndex >= 0,
                canRedo = model.currentActionIndex < model.lastActions.size - 1
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            model.memeTemplate?.let { template ->
                // TODO: Implement meme canvas with template image and draggable text boxes
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = template.resourceId,
                    contentDescription = template.name,
                    contentScale = ContentScale.Crop
                )
            }
        }

        if (model.showSaveOptions) {
            val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

            ModalBottomSheet(
                onDismissRequest = onToggleSaveOptions,
                sheetState = modalBottomSheetState,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                SaveOptionsSheet(
                    onDismiss = onToggleSaveOptions,
                    onSave = { /* TODO: Implement save to device */ },
                    onShare = { /* TODO: Implement share */ }
                )
            }

        }

        if (model.showExitDialog) {
            ExitConfirmationDialog(
                onDismiss = onToggleExitDialog,
                onConfirm = { onBackPress() }
            )
        }
    }
}