package com.group10.uxuiapp.domain
import com.group10.uxuiapp.data.TaskItem
import com.group10.uxuiapp.data.TaskList

class ListManager {
    private val _allLists = mutableListOf<TaskList>()
    fun getLists(): List<TaskList> = _allLists

    fun addList(title: String) {
        val newIndex = _allLists.size // Use the size directly
        val newList = TaskList(index = newIndex, title = title)
        _allLists.add(newList)
    }

    fun addTaskToList(title: String){
        val taskList = _allLists.find { it.title == title }

        if(taskList != null) {
            val newTask = TaskItem("", false)
            taskList.task += newTask
        }
    }

    fun removeList(index: Int) {
        _allLists.removeIf { it.index == index }

        // Reorder indices after removal to avoid gaps
        _allLists.forEachIndexed { i, taskList ->
            taskList.index = i
        }
    }

    fun updateTitle(index: Int, title: String) {
        val list = _allLists.find { it.index == index }
        list?.title = title
    }

    fun toggleLikedStatus(index: Int) {
        val list = _allLists.find { it.index == index }
        list?.isLiked = list.isLiked.not()
    }
}
