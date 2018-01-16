package io.github.rozza.todo2done

import io.github.rozza.todo2done.room.TodoEntity
import java.io.Serializable

data class TodoItem(val id: Long = 0, val title: String, val dateAdded: Long,
                    val dateCompleted: Long?, val priority: Priority) : Serializable {

    enum class Priority {
        HIGH, MEDIUM, LOW
    }

    fun toEntity(): TodoEntity {
        return TodoEntity(id, title, dateAdded, dateCompleted, priority.toString())
    }
}