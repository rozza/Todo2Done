package io.github.rozza.todo2done

import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.todo_completed_list.view.*
import java.text.SimpleDateFormat
import java.util.Locale

class TodoCompletedViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private var view = v

    fun bindTo(item: TodoItem) {
        view.completed_item_title.text = item.title
        view.completed_item_start_date.text = setCurrentDate(item.dateAdded)
        view.completed_item_end_date.text = setCurrentDate(item.dateCompleted)
        view.completed_item_number_days.text = getDaysDifference(item.dateAdded, item.dateCompleted)
        val textColor = when {
            item.priority == TodoItem.Priority.HIGH ->
                ContextCompat.getColor(view.context, R.color.priorityHigh)
            item.priority == TodoItem.Priority.MEDIUM ->
                ContextCompat.getColor(view.context, R.color.priorityMedium)
            item.priority == TodoItem.Priority.LOW ->
                ContextCompat.getColor(view.context, R.color.priorityLow)
            else -> ContextCompat.getColor(view.context, R.color.priorityLow)
        }

        setTextColor(textColor)
    }

    private fun setCurrentDate(date: Long?): String {
        val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy, HH:mm", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    private fun getDaysDifference(startDate: Long, endDate: Long?): String {
        val difference = endDate?.minus(startDate)
        var days = difference?.div(86400000)
        if (days?.compareTo(1) == -1) {
            days = 0L
        }
        return days.toString()
    }

    private fun setTextColor(@ColorInt color: Int) {
        view.completed_item_title.setTextColor(color)
        view.completed_item_start_date_label.setTextColor(color)
        view.completed_item_start_date.setTextColor(color)
        view.completed_item_end_date_label.setTextColor(color)
        view.completed_item_end_date.setTextColor(color)
        view.completed_item_days_label.setTextColor(color)
        view.completed_item_number_days.setTextColor(color)
    }

}