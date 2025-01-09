package com.group10.uxuiapp.ui.todolist.view.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.uxuiapplication.ChangeButton
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import com.group10.uxuiapp.ui.navigation.AppNavigator
import com.group10.uxuiapp.ui.todolist.view.components.buttons.IsLikedButton
import com.group10.uxuiapp.ui.todolist.viewmodel.TodoListViewModel
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


@Composable
fun TodoListCard(
    modifier: Modifier,
    todoList: TodoList,
    viewModel: TodoListViewModel,
    appNavigator: AppNavigator,
    onPositionChange: (IntOffset, TodoList) -> Unit,
    taskListsWithItems: List<TodoListWithTaskItem>
) {
    var cardGlobalOffset by remember { mutableStateOf(Offset.Zero) }
    var cardHeight by remember { mutableStateOf(0) }

    val density = LocalDensity.current

    val optionsPopupOffset = 50.dp
    val extraYOffsetPx = with(density) { optionsPopupOffset.toPx().toInt() }




    // Remember the background based on gifUrl
    if (!todoList.gifUrl.isNullOrEmpty()) {

        Modifier.fillMaxSize()
            .then(
                Modifier.background(Color.Transparent)
            )

    } else {
        Modifier.background(
            Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    Color(0xFFC0DCEF)
                ),
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .onGloballyPositioned { layoutCoordinates ->
                cardGlobalOffset = layoutCoordinates.localToRoot(Offset.Zero)
                cardHeight = layoutCoordinates.size.height
            }
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onTap = {
//                        // Normal tap
//                        val refreshedTaskList = taskListsWithItems.find { it.todoList.id == todoList.id }
//                        if (refreshedTaskList != null) {
//                            appNavigator.navigateToTask(refreshedTaskList.todoList.id)
//                        }
//                    },
//                    onLongPress = {
//                        // Calculate the position where the popup should appear (below the card)
//                        val finalOffset = IntOffset(
//                            x = 0,
//                            y = (cardGlobalOffset.y + cardHeight).toInt() - extraYOffsetPx  // adjust y offset
//                        )
//                        onPositionChange(finalOffset, todoList)
//                    }
//                )
//            }
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
        } else {
            // Default gradient background if no GIF is provided
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary,
                                Color(0xFFC0DCEF)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(0f, Float.POSITIVE_INFINITY)
                        )
                    )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column{
                Text(
                    text = todoList.title,
                    color = Color(android.graphics.Color.parseColor(todoList.textColor)),
                    modifier = Modifier.width(320.dp)
                )
                DueByDate(todoList)
            }
            IsLikedButton(todoList, onClick = {
                viewModel.updateTodoList(todoList, isLiked = !todoList.isLiked)
            })
        }
    }
}