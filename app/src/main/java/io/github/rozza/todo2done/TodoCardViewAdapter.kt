package io.github.rozza.todo2done

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.github.rozza.todo2done.utils.CompareOutstandingTodos
import io.github.rozza.todo2done.utils.inflate

class TodoCardViewAdapter(private var list: List<TodoItem>, private var changeListener: (TodoItem) -> Unit) : RecyclerView.Adapter<TodoViewHolder>() {

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bindTo(list[position], changeListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            TodoViewHolder(parent.inflate(R.layout.todo_item))

    fun updateList(list: List<TodoItem>) {
        val sortedList = list.sortedWith(CompareOutstandingTodos)
        this.list = sortedList
        notifyDataSetChanged()
    }

}