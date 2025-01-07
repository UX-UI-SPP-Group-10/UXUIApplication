import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.TaskDataSource
import com.group10.uxuiapp.data.data_class.TodoListWithTaskItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskListViewModel(private val taskDataSource: TaskDataSource) : ViewModel() {

    private val _currentTodoList = MutableStateFlow<TodoListWithTaskItem?>(null)
    val currentTodoList: StateFlow<TodoListWithTaskItem?> = _currentTodoList

    fun selectTodoList(todoListId: Int) {
        viewModelScope.launch {
            // Fetch the TodoList
            val todoListFlow = taskDataSource.getTodoListById(todoListId)
            todoListFlow.collect { todoList ->
                // Fetch associated TaskItems
                val taskItems = taskDataSource.getTaskItemsByListId(todoListId)
                    .first() // Collect tasks as a single list (non-flow)

                // Combine into a TodoListWithTaskItem
                val todoListWithTaskItem = TodoListWithTaskItem(todoList, taskItems)

                // Update the state
                _currentTodoList.value = todoListWithTaskItem
            }
        }
    }


    fun addTaskToList(taskItem: TaskItem) {
        viewModelScope.launch {
            taskDataSource.insertTaskItem(taskItem)
        }
    }

    fun updateTaskItem(taskItem: TaskItem, label: String? = null, isComplete: Boolean? = null) {
        viewModelScope.launch {
            taskDataSource.updateTaskItem(
                taskItem = taskItem,
                label = label,
                isComplete = isComplete
            )
        }
    }

    fun deleteTask(taskItem: TaskItem) {
        viewModelScope.launch {
            taskDataSource.deleteTaskItem(taskItem)
        }
    }
}
