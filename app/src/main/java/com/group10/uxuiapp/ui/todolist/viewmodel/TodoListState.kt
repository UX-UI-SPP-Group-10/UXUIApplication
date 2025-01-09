package com.group10.uxuiapp.ui.todolist.viewmodel

import com.group10.uxuiapp.data.data_class.TodoList

sealed class TodoListState {
    object None : TodoListState()
    object NewList : TodoListState()
    data class Rename(val todoList: TodoList) : TodoListState()
    data class SelectGif(val todoList: TodoList) : TodoListState()
    //data class ColorPick(val todoList: TodoList) : TodoListState()
}