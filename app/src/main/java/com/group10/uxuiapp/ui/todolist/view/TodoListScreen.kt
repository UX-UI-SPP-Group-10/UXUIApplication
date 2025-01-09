package com.group10.uxuiapp.ui.todolist.view

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.core.view.ViewCompat
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.ui.todolist.view.components.*
import com.group10.uxuiapp.ui.todolist.view.components.buttons.AddNewTodoListButton
import com.group10.uxuiapp.ui.todolist.view.components.buttons.SettingsButton
import com.group10.uxuiapp.data.GiphyActivity
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import com.group10.uxuiapp.ui.todolist.view.components.ListNameInputDialog
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

// Main TodoListScreen with Scaffold and LazyColumn
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(viewModel: TodoListViewModel, appNavigator: AppNavigator) {
    val query = remember { mutableStateOf("") }
    val popupOffset = remember { mutableStateOf(IntOffset.Zero) }

    // Colorpicker relevance
    val showColorPickerDialog = remember { mutableStateOf(false) }
    val selectedColor = remember { mutableStateOf("#FFFFFF") }

    // Collect the lists from the ViewModel's Flow
    val todoListsWithItems by viewModel.lists.collectAsState(emptyList())
    val selectedTodoList by viewModel.selectedTodoList.collectAsState()
    val popupState by viewModel.todoListState.collectAsState()
    val listPositionState = rememberLazyListState()
    val view = LocalView.current



    // Use LaunchedEffect to reset selectedIndex if list size changes
    LaunchedEffect(todoListsWithItems) {
        if (selectedTodoList != null &&
            todoListsWithItems.none { it.todoList == selectedTodoList }
        ) {
            viewModel.selectTodoList(null)
        }
    }

    // Filter the lists whenever query.value changes
    val filteredLists = remember(query.value, todoListsWithItems) {
        todoListsWithItems.filter {
            it.todoList.title.contains(query.value, ignoreCase = true)
        }
    }

    // Decide which list to show: full or filtered
    val listsToShow = if (query.value.isBlank()) todoListsWithItems else filteredLists

    var isDragging by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
            val updatedList = todoListsWithItems.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
            viewModel.updateAllListIndexes(updatedList)
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { TopAppBarWithMenu(query) },
            floatingActionButton = {
                AddNewTodoListButton {
                    viewModel.setNewlistState()
                }
            }
        ) { innerPadding  ->

            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current) + 12.dp,
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current) + 12.dp,
                    bottom = innerPadding.calculateBottomPadding() + 100.dp
                )
            ) {
                items(todoListsWithItems, key = { it.todoList.id }) { item ->
                    ReorderableItem(reorderableLazyListState, key = item.todoList.id) { isDragging ->
                        // Render cards based on the current order
                        val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                        Surface(
                            shadowElevation = elevation,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .animateItemPlacement()
                        ) {
                            TodoListCard(
                                todoList = item.todoList,
                                onPositionChange = { offset, list ->
                                    popupOffset.value = offset
                                    viewModel.selectTodoList(list)
                                },
                                viewModel = viewModel,
                                appNavigator = appNavigator,
                                taskListsWithItems = todoListsWithItems,
                                scope = this
                            )
                        }
                    }
                }

            }


        }

        OptionsPopup(
            expanded = selectedTodoList != null,
            onDismissRequest = { viewModel.selectTodoList(null) },
            onClose = {
                viewModel.selectTodoList(null)
            },
            onDelete = {
                viewModel.removeTodoList(selectedTodoList!!)
                viewModel.selectTodoList(null)
            },
            onUpdate = {
                viewModel.setRenameState(selectedTodoList!!)
            },
            onGifSelect = {
                viewModel.setSelectGifState(selectedTodoList!!)
            },
            offset = popupOffset.value,
////            onColorChange = {
////                viewModel.setColorPickState(selectedTodoList!!)
////            }
            onTagsEdit = {
                viewModel.setTagEditState(selectedTodoList!!)
            }
        )

//        // Show GiphyDialog when needed
//        if (showGiphyDialog.value) {
//            GiphyDialog(
//                context = context,
//                onGifSelected = { gifUrl ->
//                    // Update the GIF URL in ViewModel
//                    selectedTodoList?.let { todoList ->
//                        viewModel.updateGifUrl(todoList.id, gifUrl)
//                    }
//                    showGiphyDialog.value = false
//                },
//                onDismissed = {
//                    showGiphyDialog.value = false
//                }
//            )
//        }

        PopupManager(
            popupState = popupState,
            viewModel = viewModel,
            onNewListConfirm = { name ->
                viewModel.addTodoList(name)
                viewModel.setNoneState()
            },
            onRenameConfirm = { todoList, newName, selectedColor, selectedDate ->
                viewModel.updateTodoList(
                    todoList = todoList,
                    title = newName,
                    textColor = selectedColor,
                    dueDate = selectedDate
                    // Handle selectedDate if applicable
                )
                viewModel.setNoneState()
            },
            onGifSelected = { todoList, gifUrl ->
                viewModel.updateGifUrl(todoList.id, gifUrl)
                viewModel.setNoneState()
            },
            onColorSelected = { todoList, color ->
                viewModel.updateTextColor(todoList.id, color)
                viewModel.setNoneState()
            },
            onTagsEdited = { todoList, tags ->
                viewModel.updateTags(todoList.id, tags)
                viewModel.setNoneState()
            },
            onDismiss = {
                viewModel.setNoneState()
            }
        )
    }
}


// Top app bar with search and settings icons and dropdown menu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarWithMenu(query: MutableState<String>) {
    val context = LocalContext.current
    val textFieldVisible = remember { mutableStateOf(false) }

    TopAppBar(
        title = {

            if (textFieldVisible.value) {
                TextField(
                    value = query.value,
                    onValueChange = { query.value = it }, // Update the query state
                    placeholder = { Text("Search...") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .clip(RoundedCornerShape(20.dp)),

                )
            } else {
                Text(text = "")
            }
        },
        modifier = Modifier,
        navigationIcon = {
            IconButton(onClick = {
                textFieldVisible.value = !textFieldVisible.value
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        },
        actions = {
            SettingsButton(
                context = context,
                onSetting1Click = {  }, // TODO add settings 1
                onSetting2Click = {  } , // TODO add settings 2
                onSetting3Click = {  }) // TODO add settings 3
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}