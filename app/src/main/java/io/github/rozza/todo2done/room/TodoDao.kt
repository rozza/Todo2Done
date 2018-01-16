package io.github.rozza.todo2done.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Flowable

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createItem(user: TodoEntity)

    @Query("SELECT * FROM $TABLE_NAME")
    fun findAll(): Flowable<List<TodoEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE date_completed > 0")
    fun findAllCompleted(): Flowable<List<TodoEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE date_completed = 0")
    fun findAllOutstanding(): Flowable<List<TodoEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    fun findItemById(id: Long): Flowable<TodoEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(item: TodoEntity)

    @Delete
    fun deleteItem(item: TodoEntity)
}