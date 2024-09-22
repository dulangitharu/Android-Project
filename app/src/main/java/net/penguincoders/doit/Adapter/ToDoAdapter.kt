package net.penguincoders.doit.Adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.penguincoders.doit.AddNewTask
import net.penguincoders.doit.MainActivity
import net.penguincoders.doit.Model.ToDoModel
import net.penguincoders.doit.R

class ToDoAdapter(
    private val context: Context, // Context ලබා ගන්න
    private var todoList: MutableList<ToDoModel> = mutableListOf() // සදහන් කර ඇත
) : RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = todoList[position]
        holder.task.text = item.task
        holder.task.isChecked = toBoolean(item.status)

        holder.task.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            item.status = if (isChecked) 1 else 0
            saveTasks() // අලුත්ම තත්වය සුරකින්න
        }
    }

    private fun toBoolean(n: Int): Boolean {
        return n != 0
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun setTasks(todoList: List<ToDoModel>) {
        this.todoList = todoList.toMutableList()
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        todoList.removeAt(position)
        saveTasks()
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item = todoList[position]
        val bundle = Bundle().apply {
            putString("id", item.id)
            putString("task", item.task)
        }
        val fragment = AddNewTask().apply {
            arguments = bundle
        }
        // Fragment එක පෙන්වන්න
        (context as? MainActivity)?.supportFragmentManager?.let { fragment.show(it, AddNewTask.TAG) }
    }

    private fun saveTasks() {
        val editor = context.getSharedPreferences("tasks", Context.MODE_PRIVATE).edit() // Context එකෙන් SharedPreferences ලබා ගන්න
        val gson = Gson()
        val json = gson.toJson(todoList)
        editor.putString("task_list", json)
        editor.apply()
    }

    fun loadTasks() {
        val gson = Gson()
        val json = context.getSharedPreferences("tasks", Context.MODE_PRIVATE).getString("task_list", null)
        val type = object : TypeToken<MutableList<ToDoModel>>() {}.type
        todoList = gson.fromJson(json, type) ?: mutableListOf()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val task: CheckBox = view.findViewById(R.id.todoCheckBox)
    }
}
