package com.group10.uxuiapp.domain
import android.util.Log.i
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

        var startIndex = _allLists.indexOfFirst { it.index == index + 1 }

        if (startIndex != -1) {
            for (i in startIndex until _allLists.size) {
                _allLists[i].index = _allLists[i].index - 1
            }
        }
    }



    fun updateTitle(index: Int, title: String) {
        val list = _allLists.find { it.index == index }
        list?.title = title
    }
}
