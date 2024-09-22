package net.penguincoders.doit

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.penguincoders.doit.Adapters.ToDoAdapter
import net.penguincoders.doit.Model.ToDoModel

class MainActivity : AppCompatActivity(), DialogCloseListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: ToDoAdapter
    private lateinit var fab: FloatingActionButton
    private var taskList: MutableList<ToDoModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("todo_prefs", MODE_PRIVATE)

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize tasksAdapter correctly
        tasksAdapter = ToDoAdapter(this, taskList)
        tasksRecyclerView.adapter = tasksAdapter

        // Correctly pass tasksAdapter to RecyclerItemTouchHelper
        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(tasksAdapter, this))
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)

        fab = findViewById(R.id.fab)

        loadTasks()

        fab.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        }
    }

    private fun loadTasks() {
        taskList.clear()
        val tasksJson = sharedPreferences.getString("tasks", "[]") ?: "[]"
        val tasks = parseTasksJson(tasksJson)
        taskList.addAll(tasks)
        tasksAdapter.setTasks(taskList)
    }

    private fun parseTasksJson(json: String): List<ToDoModel> {
        val gson = Gson()
        val type = object : TypeToken<List<ToDoModel>>() {}.type
        return gson.fromJson(json, type) // Parse JSON to List<ToDoModel>
    }

    override fun handleDialogClose(dialog: DialogInterface) {
        loadTasks()
    }
}
