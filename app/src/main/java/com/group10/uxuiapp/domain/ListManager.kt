package com.group10.uxuiapp.domain
import com.group10.uxuiapp.data.TaskItem
import com.group10.uxuiapp.data.TaskList

class ListManager {
    private val _allLists = mutableListOf<TaskList>()

    fun addList(title: String) {
        val newIndex = _allLists.size + 1
        val newList = TaskList(index = newIndex, title = title)
        _allLists.add(newList)
    }
    fun getLists(): List<TaskList> = _allLists

    fun addTaskToList(title: String, taskItem: TaskItem){
        val taskList = _allLists.find { it.title == title }

        if(taskList != null) {
            val updateTask = taskList.task + taskItem
            taskList.task = updateTask
        }
    }

    fun removeList(index: Int) {
        _allLists.removeIf { it.index == index }
    }

    fun updateTitle(index: Int, title: String) {
        val list = _allLists.find { it.index == index }
        list?.title = title
    }
}
