import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group10.uxuiapp.data.data_class.TaskItem
import com.group10.uxuiapp.data.data_class.TodoList
import com.group10.uxuiapp.data.TaskDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskListViewModel(private val taskDataSource: TaskDataSource) : ViewModel() {
    private val _tasks = MutableStateFlow<List<TaskItem>>(emptyList())
    val tasks: StateFlow<List<TaskItem>> = _tasks

    private val _currentTodoList = MutableStateFlow<TodoList?>(null)
    val currentTodoList: StateFlow<TodoList?> = _currentTodoList

    fun loadTodoList(todoListId: Int) {
        viewModelScope.launch {
            taskDataSource.getTodoListById(todoListId).collect { todoList ->
                _currentTodoList.value = todoList
                loadTasksForTodoList(todoListId) // Load tasks whenever the TodoList changes
            }
        }
    }

    private fun loadTasksForTodoList(todoListId: Int) {
        viewModelScope.launch {
            taskDataSource.getTaskItemsByListId(todoListId).collect { taskItems ->
                _tasks.value = taskItems
            }
        }
    }

    fun addTask(label: String) {
        val todoListId = _currentTodoList.value?.id
        if (todoListId != null) {
            viewModelScope.launch {
                val newTask = TaskItem(label = label, isComplete = false, todoListId = todoListId)
                taskDataSource.insertTaskItem(newTask)
                loadTasksForTodoList(todoListId) // Refresh tasks after adding
            }
        }
    }

    fun updateTask(taskItem: TaskItem, label: String? = null, isComplete: Boolean? = null) {
        viewModelScope.launch {
            taskDataSource.updateTaskItem(
                taskItem = taskItem.copy(
                    label = label ?: taskItem.label,
                    isComplete = isComplete ?: taskItem.isComplete
                )
            )
            loadTasksForTodoList(taskItem.todoListId) // Refresh tasks after updating
        }
    }

    fun deleteTask(taskItem: TaskItem) {
        viewModelScope.launch {
            taskDataSource.deleteTaskItem(taskItem)
            loadTasksForTodoList(taskItem.todoListId) // Refresh tasks after deleting
        }
    }
}
