package io.github.rozza.todo2done

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.github.rozza.todo2done.utils.CompareOutstandingTodos
import io.github.rozza.todo2done.utils.inflate

class TodoCompletedViewAdapter(private var list: List<TodoItem>) : RecyclerView.Adapter<TodoCompletedViewHolder>() {

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TodoCompletedViewHolder?, position: Int) {
        holder?.bindTo(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TodoCompletedViewHolder(parent.inflate(R.layout.todo_completed_list))

    fun updateList(list: List<TodoItem>) {
        val sortedList = list.sortedWith(CompareOutstandingTodos)
        this.list = sortedList
        notifyDataSetChanged()
    }

}