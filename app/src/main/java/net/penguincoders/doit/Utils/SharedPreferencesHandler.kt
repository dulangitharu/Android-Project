package net.penguincoders.doit.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.penguincoders.doit.Model.ToDoModel

class SharedPreferencesHandler(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("ToDoListPreferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val gson = Gson()

    companion object {
        const val TODO_LIST_KEY = "todoList"
    }

    // Task එකක් ඇතුල් කිරීම
    fun insertTask(task: ToDoModel) {
        val taskList = getAllTasks().toMutableList() // කලින් list එක ලබාගන්න
        taskList.add(task) // Task එක add කිරීම
        saveTaskList(taskList)
        Log.d("SharedPreferencesHandler", "Task inserted: ${task.task}, Total Tasks: ${taskList.size}")
    }

    // සියලුම Task ලබා ගැනීම
    fun getAllTasks(): List<ToDoModel> {
        val json = sharedPreferences.getString(TODO_LIST_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<ToDoModel>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } else {
            emptyList() // නැත්නම් හිස් ලැයිස්තුවක් ලබා දෙයි
        }
    }

    // Task status එක යාවත්කාලීන කිරීම
    fun updateStatus(id: String, status: Int) {
        val taskList = getAllTasks().toMutableList()
        val task = taskList.find { it.id == id }
        task?.let {
            it.status = status
            saveTaskList(taskList) // යාවත්කාලීන කල ලැයිස්තුව සුරකිනවා
        } ?: run {
            Log.d("SharedPreferencesHandler", "Task with id $id not found")
        }
    }

    fun updateTask(id: String, taskDescription: String) {
        val taskList = getAllTasks().toMutableList()
        val taskItem = taskList.find { it.id == id }
        taskItem?.let {
            it.task = taskDescription
            saveTaskList(taskList)
            Log.d("SharedPreferencesHandler", "Task updated: ${it.task}")
        } ?: Log.d("SharedPreferencesHandler", "Task with id $id not found")
    }

    fun deleteTask(id: String) {
        val taskList = getAllTasks().toMutableList()
        val removed = taskList.removeIf { it.id == id }
        if (removed) {
            saveTaskList(taskList)
            Log.d("SharedPreferencesHandler", "Task with id $id deleted")
        } else {
            Log.d("SharedPreferencesHandler", "Task with id $id not found for deletion")
        }
    }


    // Task List එක SharedPreferences වලට සුරක්ෂිතව තබන ක්‍රියාව
    private fun saveTaskList(taskList: List<ToDoModel>) {
        val json = gson.toJson(taskList)
        editor.putString(TODO_LIST_KEY, json).apply() // apply() භාවිතා කර ඔබේ changes Async වේ
    }

    // සියලුම Task මකන්න
    fun clearAllTasks() {
        editor.remove(TODO_LIST_KEY).apply() // සියලුම task remove කිරීම
    }
}
