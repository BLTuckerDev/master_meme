package dev.bltucker.mastermeme.creatememe

import ExitConfirmationDialog
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import dev.bltucker.mastermeme.creatememe.composables.CreateMemeBottomBar
import dev.bltucker.mastermeme.creatememe.composables.CreateMemeTopBar
import dev.bltucker.mastermeme.creatememe.composables.EditMemeTextDialog
import dev.bltucker.mastermeme.creatememe.composables.MemeTextOverlay
import dev.bltucker.mastermeme.creatememe.composables.SaveOptionsSheet
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CreateMemeTemplateArgs(val templateId: Int)

fun NavGraphBuilder.createMemeScreen(onNavigateBack: () -> Unit) {
    composable<CreateMemeTemplateArgs>{ backStackEntry ->
        val args = backStackEntry.toRoute<CreateMemeTemplateArgs>()
        val viewModel = hiltViewModel<CreateMemeViewModel>()
        val model by viewModel.observableModel.collectAsStateWithLifecycle()

        LifecycleStartEffect(Unit) {
            viewModel.onStart(args.templateId)

            onStopOrDispose {  }
        }


        BackHandler(onBack = {
            viewModel.onToggleExitDialog()
        })

        CreateMemeScreen(
            modifier = Modifier.fillMaxSize(),
            model = model,
            onBackPress = onNavigateBack,
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
            onSaveMeme = viewModel::onSaveMeme,

            onShowEditMemeTextDialog = viewModel::onShowEditMemeTextDialog,
            onHideEditMemeTextDialog = viewModel::onHideEditMemeTextDialog,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeApi::class
)
@Composable
fun CreateMemeScreen(
    model: CreateMemeModel,
    onBackPress: () -> Unit,

    onShowEditMemeTextDialog: () -> Unit,
    onHideEditMemeTextDialog: () -> Unit,

    onAddTextBox: (MemeTextBox) -> Unit,
    onTextBoxSelected: (MemeTextBox) -> Unit,
    onTextBoxMoved: (MemeTextBox, Offset) -> Unit,
    onTextBoxDeleted: (MemeTextBox) -> Unit,
    onUpdateTextBoxText: (MemeTextBox, String) -> Unit,
    onUpdateTextBoxFontSize: (MemeTextBox, TextUnit) -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onToggleSaveOptions: () -> Unit,
    onToggleExitDialog: () -> Unit,
    onSaveMeme: (Bitmap) -> Unit,
    modifier: Modifier = Modifier
) {

    var lastClickOffset by remember { mutableStateOf(Offset.Zero) }
    val captureController = rememberCaptureController()
    val coroutineScope = rememberCoroutineScope()


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
                onAddTextBox = {
                    lastClickOffset = Offset.Zero
                    onShowEditMemeTextDialog()
                },
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
                Box(modifier = Modifier
                    .capturable(captureController)
                    .fillMaxSize()) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures { offset ->
                                    lastClickOffset = offset
                                    onShowEditMemeTextDialog()
                                }
                            },
                        model = template.resourceId,
                        contentDescription = template.name,
                        contentScale = ContentScale.FillWidth
                    )

                    model.textBoxes.forEach { textBox ->
                        MemeTextOverlay(
                            text = textBox.text,
                            offset = textBox.position,
                            fontSize = textBox.fontSize,
                            fontFamily = textBox.fontFamily,
                            color = textBox.color,
                            isSelected = textBox == model.selectedTextBox,
                            onDelete = { onTextBoxDeleted(textBox) },
                            onTap = { onTextBoxSelected(textBox) }
                        )
                    }
                }
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
                    onSave = {
                        coroutineScope.launch {
                            try {
                                val bitmapAsync = captureController.captureAsync()
                                val bitmap = bitmapAsync.await()
                                onSaveMeme(bitmap.asAndroidBitmap())
                            } catch (error: Exception) {
                                Log.d("DEBUG", "error saving: $error")
                            }
                        }
                    },
                    onShare = { /* TODO: Implement share */ }
                )
            }

        }

        if (model.showExitDialog) {
            ExitConfirmationDialog(
                onDismiss = onToggleExitDialog,
                onConfirm = { onBackPress() }
            )
        } else if(model.showEditMemeTextDialog){
            EditMemeTextDialog(
                text = "",
                onDismiss = {
                    lastClickOffset = Offset.Zero
                    onHideEditMemeTextDialog()
                },
                onSave = { memeText ->

                    val textBox = MemeTextBox(
                        id = UUID.randomUUID().toString(),
                        text = memeText,
                        position = lastClickOffset,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Default,
                        color = Color.White
                    )

                    lastClickOffset = Offset.Zero

                    onAddTextBox(textBox)
                },
            )
        }
    }
}