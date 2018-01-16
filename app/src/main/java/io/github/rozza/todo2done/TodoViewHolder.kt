package io.github.rozza.todo2done

import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.todo_item.view.*
import java.text.SimpleDateFormat
import java.util.Locale

class TodoViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private var view = v

    fun bindTo(item: TodoItem, listener: (TodoItem) -> Unit) {
        view.item_title.text = item.title
        view.item_date.text = setCurrentDate(item.dateAdded)
        val backgroundColor = when {
                    item.priority == TodoItem.Priority.HIGH ->
                        getColor(view.context, R.color.priorityHigh)
                    item.priority == TodoItem.Priority.MEDIUM ->
                        getColor(view.context, R.color.priorityMedium)
                    item.priority == TodoItem.Priority.LOW ->
                        getColor(view.context, R.color.priorityLow)
                    else -> getColor(view.context, R.color.priorityLow)
                }

        view.item_holder.setBackgroundColor(backgroundColor)
        // clears the view holder so that when it is reused it is not checked
        view.item_check_box.setOnCheckedChangeListener(null)
        view.item_check_box.isChecked = false
        view.item_check_box.setOnCheckedChangeListener { _, _ ->
            listener(item)
        }
    }

    private fun setCurrentDate(date: Long): String {
        val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy, HH:mm", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

}