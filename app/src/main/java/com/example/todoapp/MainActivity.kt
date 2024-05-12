package com.example.todoapp

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.adapters.TaskRVViewBindingAdapter
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.models.Task
import com.example.todoapp.utils.LongToastShow
import com.example.todoapp.utils.Status
import com.example.todoapp.utils.clearEditText
import com.example.todoapp.utils.setupDialog
import com.example.todoapp.utils.validateEditText
import com.example.todoapp.utils.validatePriority
import com.example.todoapp.viewmodals.TaskViewModal
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val addTaskDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.add_task_dialog)
        }
    }
    private val updateTaskDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.update_task_dialog)
        }
    }
    private val loadingDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.loading_dialog)
        }
    }
    private val taskViewModal: TaskViewModal by lazy {
        ViewModelProvider(this)[TaskViewModal::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)


        val addCloseImg = addTaskDialog.findViewById<ImageView>(R.id.closeImg)
        val updateCloseImg = updateTaskDialog.findViewById<ImageView>(R.id.closeImg)


        //Add
        addCloseImg.setOnClickListener {
            addTaskDialog.dismiss()
        }

        val addTaskLevel = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskLevel)
        val addTaskLevelL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskLevelL)

        addTaskLevel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                val isEditTextValid = validateEditText(addTaskLevel, addTaskLevelL)
                if (isEditTextValid) {
                    validatePriority(addTaskLevel.text.toString(), addTaskLevelL)
                }
            }
        })

        val addETTitle = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val addETTitleL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        addETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETTitle, addETTitleL)
            }

        })

        val addETDesc = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val addETDescL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)

        addETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETDesc, addETDescL)
            }
        })


        mainBinding.addTaskFABtn.setOnClickListener {
            clearEditText(addTaskLevel, addTaskLevelL)
            clearEditText(addETTitle, addETTitleL)
            clearEditText(addETDesc, addETDescL)
            addTaskDialog.show()

        }

        val saveTaskBtn = addTaskDialog.findViewById<Button>(R.id.saveTaskBtn)

        saveTaskBtn.setOnClickListener {
            if (validateEditText(
                    addTaskLevel,
                    addTaskLevelL
                ) && validatePriority(addTaskLevel.text.toString(), addTaskLevelL) && validateEditText(
                    addETTitle,
                    addETTitleL
                ) && validateEditText(
                    addETDesc,
                    addETDescL
                )
            ) {
                addTaskDialog.dismiss()
                val newTask = Task(
                    UUID.randomUUID().toString(),
                    addETTitle.text.toString().trim(),
                    addETDesc.text.toString().trim(),
                    addTaskLevel.text.toString().trim(),
                    Date()
                )
                taskViewModal.insertTask(newTask).observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                            loadingDialog.show()
                        }

                        Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            if (it.data?.toInt() != -1) {
                                LongToastShow("The task is added successfully")
                            }
                        }

                        Status.ERROR -> {
                            loadingDialog.dismiss()
                            it.message?.let { it1 -> LongToastShow(it1) }
                        }
                    }
                }
            }
        }

        //Update

        val updateTaskLevel = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskLevel)
        val updateTaskLevelL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskLevelL)

        updateTaskLevel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                val isEditTextValid = validateEditText(updateTaskLevel, updateTaskLevelL)
                if (isEditTextValid) {
                    validatePriority(updateTaskLevel.text.toString(), updateTaskLevelL)
                }
            }
        })


        val updateETTitle = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val updateETTitleL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        updateETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETTitle, updateETTitleL)
            }

        })

        val updateETDesc = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val updateETDescL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)

        updateETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETDesc, updateETDescL)
            }
        })

        updateCloseImg.setOnClickListener {
            updateTaskDialog.dismiss()
        }

        val updateTaskBtn = updateTaskDialog.findViewById<Button>(R.id.updateTaskBtn)

        val taskRecyclerViewAdapter = TaskRVViewBindingAdapter(this) { type, position, task ->
            if (type == "delete") {
                taskViewModal.deleteTaskUsingID(task.id)
                    .observe(this) {
                        when (it.status) {
                            Status.LOADING -> {
                                loadingDialog.show()
                            }

                            Status.SUCCESS -> {
                                loadingDialog.dismiss()
                                if (it.data != -1) {
                                    LongToastShow("The task is deleted successfully")
                                }
                            }

                            Status.ERROR -> {
                                loadingDialog.dismiss()
                                it.message?.let { it1 -> LongToastShow(it1) }
                            }
                        }
                    }
            } else if (type == "update") {
                updateTaskLevel.setText(task.level)
                updateETTitle.setText(task.title)
                updateETDesc.setText(task.description)
                updateTaskBtn.setOnClickListener {
                    if (validateEditText(
                            updateTaskLevel,
                            updateTaskLevelL
                        ) && validatePriority(updateTaskLevel.text.toString(), updateTaskLevelL) &&
                        validateEditText(updateETTitle, updateETTitleL) && validateEditText(
                            updateETDesc,
                            updateETDescL
                        )
                    ) {
                        val updateTask = Task(
                            task.id,
                            updateTaskLevel.text.toString().trim(),
                            updateETTitle.text.toString().trim(),
                            updateETDesc.text.toString().trim(),
                            Date()
                        )
                        updateTaskDialog.dismiss()
                        loadingDialog.show()
                        taskViewModal
                            .updateParticularTaskField(
                                task.id,
                                updateTaskLevel.text.toString().trim(),
                                updateETTitle.text.toString().trim(),
                                updateETDesc.text.toString().trim(),
                            )
                            .observe(this) {
                                when (it.status) {
                                    Status.LOADING -> {
                                        loadingDialog.show()
                                    }

                                    Status.SUCCESS -> {
                                        loadingDialog.dismiss()
                                        if (it.data != -1) {
                                            LongToastShow("The task is updated successfully")
                                        }
                                    }

                                    Status.ERROR -> {
                                        loadingDialog.dismiss()
                                        it.message?.let { it1 -> LongToastShow(it1) }
                                    }
                                }
                            }
                    }
                }
                updateTaskDialog.show()
            }
        }
        mainBinding.taskRV.adapter = taskRecyclerViewAdapter
        callGetTaskList(taskRecyclerViewAdapter)
    }

    private fun callGetTaskList(taskRecyclerViewAdapter: TaskRVViewBindingAdapter) {
        CoroutineScope(Dispatchers.Main).launch {
            loadingDialog.show()
            taskViewModal.getTaskList().collect {
                when (it.status) {
                    Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Status.SUCCESS -> {
                        it.data?.collect { taskList ->
                            loadingDialog.dismiss()
                            taskRecyclerViewAdapter.addAllTask(taskList)
                        }

                    }

                    Status.ERROR -> {
                        loadingDialog.dismiss()
                        it.message?.let { it1 -> LongToastShow(it1) }
                    }
                }
            }
        }
    }
}