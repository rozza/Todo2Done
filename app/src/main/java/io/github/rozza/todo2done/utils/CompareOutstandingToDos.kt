package io.github.rozza.todo2done.utils

import io.github.rozza.todo2done.TodoItem

class CompareOutstandingTodos {
    companion object : Comparator<TodoItem> {

        override fun compare(a: TodoItem, b: TodoItem): Int = when {
            a.priority != b.priority -> a.priority.ordinal - b.priority.ordinal
            a.dateAdded != b.dateAdded -> (a.dateAdded - b.dateAdded).toInt()
            else -> a.title.compareTo(b.title)
        }
    }
}