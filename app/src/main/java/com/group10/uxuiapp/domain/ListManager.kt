package com.group10.uxuiapp.domain
import com.group10.uxuiapp.data.TaskList

class ListManager {
    private val _allLists = mutableListOf<TaskList>()

    fun addList(title: String) {
        val newIndex = _allLists.size + 1
        val newList = TaskList(index = newIndex, title = title)
        _allLists.add(newList)
    }
    fun getLists(): List<TaskList> = _allLists

    fun removeList(index: Int) {
        _allLists.removeIf { it.index == index }
    }
}
