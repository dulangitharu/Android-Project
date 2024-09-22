package net.penguincoders.doit.Model

data class ToDoModel(
    val id: String,         // Unique identifier for the task
    var task: String,    // The task description
    var status: Int,      // Status of the task (0 for incomplete, 1 for complete)
) {
    // Function to toggle the status of the task
    fun toggleStatus() {
        status = if (status == 0) 1 else 0
    }

    // Function to get a human-readable status
    fun getStatusText(): String {
        return if (status == 0) "Incomplete" else "Complete"
    }
}
