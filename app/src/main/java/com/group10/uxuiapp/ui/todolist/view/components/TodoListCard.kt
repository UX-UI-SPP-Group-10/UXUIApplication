package com.group10.uxuiapp.ui.todolist.view.components

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.ui.todolist.view.components.buttons.IsLikedButton
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.core.view.ViewCompat
import com.group10.uxuiapp.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableCollectionItemScope


@Composable
fun TodoListCard(
    elevation: androidx.compose.ui.unit.Dp,
    todoList: TodoList,
    viewModel: TodoListViewModel,
    appNavigator: AppNavigator,
    onPositionChange: (IntOffset, TodoList) -> Unit,
    scope: ReorderableCollectionItemScope,
    modifier: Modifier = Modifier,
    focusManager: FocusManager
    ) {
    var cardGlobalOffset by remember { mutableStateOf(Offset.Zero) }
    var cardHeight by remember { mutableStateOf(0) }

    val density = LocalDensity.current

    val optionsPopupOffset = 50.dp
    val extraYOffsetPx = with(density) { optionsPopupOffset.toPx().toInt() }
    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }

    val focusRequester = remember { FocusRequester() }

    val newTodoListId by viewModel.newTodoListId.collectAsState()
    LaunchedEffect(newTodoListId) {
        if (newTodoListId == todoList.id) {
            focusRequester.requestFocus()
        }
    }

    val scale by animateFloatAsState(targetValue = if (elevation > 10.dp) 1.03f else 1f)

    var textValue by remember(todoList.title) { mutableStateOf(todoList.title) }
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    val backgroundColor = remember(todoList.backgroundColor) {
        todoList.backgroundColor?.let { Color(android.graphics.Color.parseColor(it)) }
    }

    val effectiveBackgroundColor = when {
        !todoList.gifUrl.isNullOrEmpty() -> Color.Transparent
        backgroundColor != null -> backgroundColor
        else -> MaterialTheme.colorScheme.surface
    }



    // Remember the background based on gifUrl
    if (!todoList.gifUrl.isNullOrEmpty()) {

        Modifier
            .fillMaxSize()
            .then(
                Modifier.background(Color.Transparent)
            )
    }
    else {
        Modifier.background(
            Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.colorScheme.tertiary,
                ),
                start = Offset(-10f, 0f),
                end = Offset(10f, Float.POSITIVE_INFINITY)
            )
        )
    }

    Surface(
        modifier = with(scope) {
            modifier
                .fillMaxWidth()
                .height(100.dp)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .onGloballyPositioned { layoutCoordinates ->
                    cardGlobalOffset = layoutCoordinates.localToRoot(Offset.Zero)
                    cardHeight = layoutCoordinates.size.height
                }
                .longPressDraggableHandle(
                    onDragStarted = {
                        Log.d("TodoListCard", "Drag started for ${todoList.id}")
                        ViewCompat.performHapticFeedback(
                            view,
                            HapticFeedbackConstantsCompat.GESTURE_START
                        )
                    },
                    onDragStopped = {
                        Log.d("TodoListCard", "Drag stopped for ${todoList.id}")
                        ViewCompat.performHapticFeedback(
                            view,
                            HapticFeedbackConstantsCompat.GESTURE_END
                        )
                    },
                    interactionSource = interactionSource
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            viewModel.resetNewTodoList()
                            focusManager.clearFocus() // Only clear focus if not read-only
                            appNavigator.navigateToTask(todoList.id)
                        }
                    )
                }
        },
        shape = RoundedCornerShape(20.dp),
        shadowElevation = elevation,
        color = effectiveBackgroundColor

    ) {
        // GIF as background (placed first to be behind everything else)
        if (!todoList.gifUrl.isNullOrEmpty()) {
            val gifPainter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(todoList.gifUrl)
                    .decoderFactory(GifDecoder.Factory())
                    .build()
            )
            Image(
                painter = gifPainter,
                contentDescription = "GIF Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Ensures the GIF fills the box area
            )
        } else if (backgroundColor == null) {
            // Default gradient background if no GIF is provided
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.tertiary,
                            ),
                            start = Offset(-10f, 0f),
                            end = Offset(10f, Float.POSITIVE_INFINITY)
                        )
                    )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Title and Due Date Column
            Column(
                modifier = Modifier.fillMaxHeight()    //fillMaxSize(0.7f)
            ) {

                BasicTextField(
                    value = textValue,
                    onValueChange = { newText ->
                        if (newText.length <= 20) {
                            textValue = newText

                            debounceJob?.cancel()
                            debounceJob = coroutineScope.launch {
                                delay(200)
                                viewModel.updateTodoList(id = todoList.id, title = newText)
                            }
                        }
                    },
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 22.sp,
                        color = Color(android.graphics.Color.parseColor(todoList.textColor)),
                    ),
                    singleLine = true,
                    modifier = modifier
                        .focusRequester(focusRequester)
                        ,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            viewModel.resetNewTodoList()
                        }
                    ),
                    decorationBox = { innerTextField ->
                            // Display placeholder when text is empty
                            if (textValue.isEmpty()) {
                                Text(
                                    text = "New List",  // Your placeholder text
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontSize = 22.sp,
                                        color = Color.Gray.copy(alpha = 0.5f),
                                    ),
                                )
                            }
                            // Display the actual text field (where text is typed)
                            innerTextField()
                    }
                )

                DueByDate(todoList = todoList)

                TagsDisplay(tags = todoList.tags, color = Color(android.graphics.Color.parseColor(todoList.textColor)))
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier.size(30.dp),
                ) {
                    if (todoList.isRepeating) { // Check if the list is repeatable
                        Icon(
                            painter = painterResource(id = R.drawable.repeat),
                            contentDescription = "Repeat",
                            tint = Color(android.graphics.Color.parseColor(todoList.textColor)),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Box(modifier = Modifier
                    .size(30.dp)
                    .clickable {
                    val finalOffset = IntOffset(
                        x = 0,
                        y = (cardGlobalOffset.y + cardHeight).toInt() - extraYOffsetPx
                    )
                    onPositionChange(finalOffset, todoList)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        modifier = Modifier
                            .size(27.dp)
                            .align(Alignment.Center),
                        tint = Color(android.graphics.Color.parseColor(todoList.textColor))
                    )
                }

                IsLikedButton(todoList, onClick = {
                    viewModel.updateTodoList(todoList.id, isLiked = !todoList.isLiked)
                    }
                )
            }
        }

    }
}