package io.github.rozza.todo2done

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.github.rozza.todo2done.room.TodoDatabase
import io.github.rozza.todo2done.room.TodoEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.todo_completed.*
import javax.inject.Inject

class CompletedActivity : AppCompatActivity() {

    @Inject
    lateinit var roomTodoDatabase: TodoDatabase
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: TodoCompletedViewAdapter
    private var itemList: MutableList<TodoItem> = ArrayList()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            item?.itemId == android.R.id.home -> {
                onBackPressed()
                true
            }
            item?.itemId == R.id.delete_forever -> {
                showDeleteAlert()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_completed)
        App.component.inject(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.completed_activity)

        // load data from Room
        loadList()

        // initialise the recyclerView
        linearLayoutManager = LinearLayoutManager(this)
        completed_todo_view.layoutManager = linearLayoutManager
        adapter = TodoCompletedViewAdapter(itemList)
        completed_todo_view.adapter = adapter
    }

    private fun loadList() {
        roomTodoDatabase.todoDao().findAllCompleted()
                .subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { results ->
                    itemList = convertTodoEntityListToTodo(results)
                    adapter.updateList(itemList)
                    // remove the empty placeholder if there are items returned in list
                    if (results.isNotEmpty()) {
                        completed_todo_view.visibility = View.VISIBLE
                        completed_empty_view.visibility = View.GONE
                    }
                }
    }

    private fun convertTodoEntityListToTodo(list: List<TodoEntity>): MutableList<TodoItem> {
        // iterate through list and convert
        val newList: MutableList<TodoItem> = ArrayList()
        list.forEach {
            newList.add(TodoItem(it.id, it.title, it.dateAdded, it.dateCompleted, TodoItem.Priority.valueOf(it.priority)))
        }
        return newList
    }

    private fun showDeleteAlert() {
        // Only show alert if there's something to delete
        if (itemList.isNotEmpty()) {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setMessage(getString(R.string.delete_all))
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), { _, _ ->
                deleteAll()
            })
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), { dialogInterface, _ ->
                dialogInterface.cancel()
            })
            alertDialog.show()
        }
    }

    private fun deleteAll() {
        // iterate through the completed item list and delete
        itemList.forEach {
            val entity = TodoEntity(it.id, it.title, it.dateAdded, it.dateCompleted, it.priority.toString())
            Single.fromCallable { roomTodoDatabase.todoDao().deleteItem(entity) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }
        // clear the list stored in the global variable
        itemList.clear()
        // update that UI
        adapter.updateList(itemList)
        completed_todo_view.visibility = View.GONE
        completed_empty_view.visibility = View.VISIBLE
    }
}