package com.example.dolist

data class ToDo(
    val title: String,
    var id: Int = 0,
    var isChecked: Boolean = false
)