package com.group10.uxuiapp.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.group10.uxuiapp.domain.ListManager
import com.group10.uxuiapp.data.TaskList

class ListViewModel : ViewModel() {

    private val listManager = ListManager()
    var lists = mutableStateOf<List<TaskList>>(emptyList()) // List that's observed by UI. Connects to the domain layer

    init {
        lists.value = listManager.getLists()
    }

    fun addList(title: String) {
        listManager.addList(title) // add new list with name
        lists.value = listManager.getLists() // Update UI lists
    }
}
