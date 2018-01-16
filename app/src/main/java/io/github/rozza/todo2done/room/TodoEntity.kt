package io.github.rozza.todo2done.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const val TABLE_NAME = "todos"

@Entity(tableName = TABLE_NAME)
data class TodoEntity constructor(@ColumnInfo(name = "id")
                                  @PrimaryKey(autoGenerate = true) val id: Long,
                                  @ColumnInfo(name = "title") val title: String,
                                  @ColumnInfo(name = "date_added") val dateAdded: Long,
                                  @ColumnInfo(name = "date_completed") val dateCompleted: Long?,
                                  @ColumnInfo(name = "priority") val priority: String)