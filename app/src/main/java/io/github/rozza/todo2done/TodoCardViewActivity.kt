package io.github.rozza.todo2done

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.github.rozza.todo2done.room.TodoDatabase
import io.github.rozza.todo2done.room.TodoEntity
import io.github.rozza.todo2done.utils.getCurrentDate
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.todo_card_view_activity.*
import javax.inject.Inject
import android.support.v7.widget.DividerItemDecoration



class TodoCardViewActivity : AppCompatActivity(), TodoAddDialog.SaveNewTodoItem {

    @Inject
    lateinit var roomTodoDatabase: TodoDatabase
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: TodoCardViewAdapter
    private var todoList: MutableList<TodoItem> = ArrayList()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_past, menu)
        return true
    }

    override fun onOptionsItemSelected(todo: MenuItem?): Boolean {
        if (todo?.itemId == R.id.view_completed) {
            startActivity(Intent(this, CompletedActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(todo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_card_view_activity)
        App.component.inject(this)

        // set up add button
        add_button.setOnClickListener {
            val pop = TodoAddDialog()
            val fm = this@TodoCardViewActivity.fragmentManager
            pop.show(fm, "add")
        }

        // load list saved in Room
        loadList()

        // initialise todo_card_view
        linearLayoutManager = LinearLayoutManager(this)
        todo_card_view.layoutManager = linearLayoutManager
        adapter = TodoCardViewAdapter(todoList, this::onItemChecked)
        todo_card_view.adapter = adapter

        todo_card_view.addItemDecoration(DividerItemDecoration(todo_card_view.context,
                linearLayoutManager.orientation))
    }

    private fun onItemChecked(todo: TodoItem) {
        todoList.remove(todo)
        adapter.updateList(todoList)
        // show empty placeholder if there are no todos left in the list
        if (todoList.isEmpty()) {
            todo_card_view.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
        }
        markItemAsDone(todo)
    }

    private fun markItemAsDone(todo: TodoItem) {
        // add a completed time stamp and update todo in Room
        val entity = todo.toEntity().copy(dateCompleted = getCurrentDate())
        Single.fromCallable { roomTodoDatabase.todoDao().updateItem(entity) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun addItem(todo: TodoItem) {
        Single.fromCallable { roomTodoDatabase.todoDao().createItem(todo.toEntity()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun loadList() {
        roomTodoDatabase.todoDao().findAllOutstanding()
                .subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { results ->
                    todoList = convertTodoEntityListToTodo(results)
                    adapter.updateList(todoList)
                    // clear the empty placeholder if there are todos returned
                    if (results.isNotEmpty()) {
                        todo_card_view.visibility = View.VISIBLE
                        empty_view.visibility = View.GONE
                    }
                }
    }

    private fun convertTodoEntityListToTodo(list: List<TodoEntity>): MutableList<TodoItem> {
        val newList: MutableList<TodoItem> = ArrayList()
        // iterate through the list
        list.mapTo(newList) { it -> TodoItem(it.id, it.title, it.dateAdded, it.dateCompleted, TodoItem.Priority.valueOf(it.priority)) }
        return newList
    }
}
