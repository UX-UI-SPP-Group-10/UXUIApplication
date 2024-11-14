package com.group10.uxuiapp.domain
import com.group10.uxuiapp.data.TaskList

class ListManager {
    private val _allLists = mutableListOf<TaskList>()

    fun addList(title: String) {
        val newList = TaskList(title = title)
        _allLists.add(newList)
    }
    fun getLists(): List<TaskList> = _allLists
}
