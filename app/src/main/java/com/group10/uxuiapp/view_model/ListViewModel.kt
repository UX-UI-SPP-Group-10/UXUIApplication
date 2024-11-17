package com.group10.uxuiapp.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.group10.uxuiapp.data.TaskItem
import com.group10.uxuiapp.data.TaskList
import com.group10.uxuiapp.domain.ListManager

class ListViewModel : ViewModel() {

    private val listManager = ListManager()
    var lists = mutableStateOf<List<TaskList>>(emptyList()) // List that's observed by UI. Connects to the domain layer

    init {
        lists.value = listManager.getLists()
    }

    fun addList(title: String) {
        listManager.addList(title)
        lists.value = listManager.getLists()
    }

    fun addTaskToList(taskListIndex: Int, newTask: TaskItem) {
        lists.value[taskListIndex].taskItemList.add(newTask)
        lists.value = listManager.getLists()
   }



    fun removeList(index: Int) {
        listManager.removeList(index)
        lists.value = listManager.getLists()
    }

    fun updateTitle(index: Int, title: String) {
        listManager.updateTitle(index, title)
        lists.value = listManager.getLists()
    }

    fun toggleLikedStatus(index: Int) {
        listManager.toggleLikedStatus(index) // Toggle liked status in ListManager
        lists.value = listManager.getLists() // Update UI lists
    }
}