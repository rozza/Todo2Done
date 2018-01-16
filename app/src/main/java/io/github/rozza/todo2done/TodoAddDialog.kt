package io.github.rozza.todo2done

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import io.github.rozza.todo2done.utils.getCurrentDate

class TodoAddDialog : DialogFragment() {

    interface SaveNewTodoItem {
        fun addItem(item: TodoItem)
    }

    private lateinit var prority_list: Spinner
    private lateinit var titleTextInput: TextInputLayout
    private lateinit var prioritySelection: TodoItem.Priority
    private lateinit var listener: SaveNewTodoItem

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.todo_add_item, null)
        titleTextInput = view.findViewById(R.id.input_item_name_label)
        prority_list = view.findViewById(R.id.prority_list)
        val listOfItems = resources.getStringArray(R.array.priority_levels)
        val arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, listOfItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prority_list.adapter = arrayAdapter
        prority_list.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
                val selectedText = arg0.getItemAtPosition(position)
                prioritySelection = when (selectedText) {
                    getString(R.string.high) -> TodoItem.Priority.HIGH
                    getString(R.string.medium) -> TodoItem.Priority.MEDIUM
                    getString(R.string.low) -> TodoItem.Priority.LOW
                    else -> TodoItem.Priority.LOW
                }
            }
            override fun onNothingSelected(arg0: AdapterView<*>) {
                prioritySelection = TodoItem.Priority.LOW
            }

        }

        return AlertDialog.Builder(activity)
                .setTitle(R.string.add_item)
                .setView(view)
                .setNegativeButton(android.R.string.cancel, { d, _ ->
                    d.cancel()
                })
                .setPositiveButton(android.R.string.ok, { d, _ ->
                    listener.addItem(addItemToList())
                    d.dismiss()
                })
                .create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = when (context) {
            is SaveNewTodoItem -> context
            else -> throw ClassCastException(context.toString() + " must implement SaveNewTodoItem.")
        }
    }

    private fun addItemToList() = TodoItem(0, getTitleToString(), getCurrentDate(), 0, prioritySelection)

    private fun getTitleToString(): String {
        return titleTextInput.editText!!.text.toString()
    }

}