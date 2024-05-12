package com.example.todoapp

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.adapters.TaskRecyclerViewAdapter
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.models.Task
import com.example.todoapp.utils.LongToastShow
import com.example.todoapp.utils.Status
import com.example.todoapp.utils.clearEditText
import com.example.todoapp.utils.setupDialog
import com.example.todoapp.utils.validateEditText
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

    private val taskRecyclerViewAdapter:TaskRecyclerViewAdapter by lazy{
        TaskRecyclerViewAdapter()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        mainBinding.taskRV.adapter=taskRecyclerViewAdapter


        val addCloseImg = addTaskDialog.findViewById<ImageView>(R.id.closeImg)
        val updateCloseImg = updateTaskDialog.findViewById<ImageView>(R.id.closeImg)


        //Add
        addCloseImg.setOnClickListener {
            addTaskDialog.dismiss()
        }

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
            clearEditText(addETTitle, addETTitleL)
            clearEditText(addETDesc, addETDescL)
            addTaskDialog.show()

        }

        val saveTaskBtn = addTaskDialog.findViewById<Button>(R.id.saveTaskBtn)

        saveTaskBtn.setOnClickListener {
            if (validateEditText(addETTitle, addETTitleL) && validateEditText(
                    addETDesc,
                    addETDescL
                )
            ) {
                addTaskDialog.dismiss()
                val newTask = Task(
                    UUID.randomUUID().toString(),
                    addETTitle.text.toString().trim(),
                    addETDesc.text.toString().trim(),
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

        updateTaskBtn.setOnClickListener {
            if (validateEditText(updateETTitle, updateETTitleL) && validateEditText(
                    updateETDesc,
                    updateETDescL
                )
            ) {
                updateTaskDialog.dismiss()
                Toast.makeText(this, "Validated!!", Toast.LENGTH_SHORT).show()
                loadingDialog.show()
            }
        }


        callGetTaskList()
    }

    private fun callGetTaskList() {
        CoroutineScope(Dispatchers.Main).launch {
            loadingDialog.show()
            taskViewModal.getTaskList().collect {
                when (it.status) {
                    Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Status.SUCCESS -> {
                        it.data?.collect {taskList->
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