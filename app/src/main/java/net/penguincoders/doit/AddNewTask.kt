package net.penguincoders.doit

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.UUID

class AddNewTask : BottomSheetDialogFragment() {

    private lateinit var newTaskText: EditText
    private lateinit var newTaskSaveButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_task, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newTaskText = requireView().findViewById(R.id.newTaskText)
        newTaskSaveButton = requireView().findViewById(R.id.newTaskButton)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("ToDoPrefs", Context.MODE_PRIVATE)

        var isUpdate = false

        val bundle = arguments
        if (bundle != null) {
            isUpdate = true
            val task = bundle.getString("task")
            newTaskText.setText(task)
            task?.let {
                if (it.isNotEmpty()) {
                    newTaskSaveButton.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
                    )
                }
            }
        }

        newTaskText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    newTaskSaveButton.isEnabled = false
                    newTaskSaveButton.setTextColor(Color.GRAY)
                } else {
                    newTaskSaveButton.isEnabled = true
                    newTaskSaveButton.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        val finalIsUpdate = isUpdate
        newTaskSaveButton.setOnClickListener {
            val text = newTaskText.text.toString()

            if (finalIsUpdate) {
                // Update the existing task
                bundle?.getInt("id")?.let { id ->
                    updateTask(id, text)
                }
            } else {
                // Add a new task
                addNewTask(text)
            }
            dismiss()
        }
    }

    private fun addNewTask(taskText: String) {
        val taskId = UUID.randomUUID().toString()  // Using UUID to generate a unique ID
        sharedPreferences.edit().apply {
            putString(taskId, taskText)
            apply()
        }
    }

    private fun updateTask(taskId: Int, newText: String) {
        val editor = sharedPreferences.edit()
        val key = sharedPreferences.all.keys.elementAtOrNull(taskId)
        key?.let {
            editor.putString(it, newText)
            editor.apply()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        val activity = activity
        if (activity is DialogCloseListener) {
            activity.handleDialogClose(dialog)
        }
    }
}
