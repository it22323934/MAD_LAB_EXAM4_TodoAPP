package com.example.todoapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todoapp.adapters.TaskRVVBListAdapter
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.models.Task
import com.example.todoapp.utils.LongToastShow
import com.example.todoapp.utils.Status
import com.example.todoapp.utils.StatusResult
import com.example.todoapp.utils.StatusResult.*
import com.example.todoapp.utils.clearEditText
import com.example.todoapp.utils.hideKeyBoard
import com.example.todoapp.utils.setupDialog
import com.example.todoapp.utils.validateEditText
import com.example.todoapp.utils.validatePriority
import com.example.todoapp.viewmodals.TaskViewModal
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
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

    private val isListMutableLiveData=MutableLiveData<Boolean>().apply {
        postValue(true)
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
                val newTask = Task(
                    UUID.randomUUID().toString(),
                    addETTitle.text.toString().trim(),
                    addETDesc.text.toString().trim(),
                    addTaskLevel.text.toString().trim(),
                    Date()
                )
                hideKeyBoard(it)
                addTaskDialog.dismiss()
                taskViewModal.insertTask(newTask)
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

        isListMutableLiveData.observe(this){
            if (it){
                mainBinding.taskRV.layoutManager = LinearLayoutManager(
                    this,LinearLayoutManager.VERTICAL,false
                )
                mainBinding.listOrGridImg.setImageResource(R.drawable.baseline_grid_view_24)
            }else{
                mainBinding.taskRV.layoutManager = StaggeredGridLayoutManager(
                    2,LinearLayoutManager.VERTICAL
                )
                mainBinding.listOrGridImg.setImageResource(R.drawable.baseline_list_alt_24)
            }
        }

        mainBinding.listOrGridImg.setOnClickListener {
            isListMutableLiveData.postValue(!isListMutableLiveData.value!!)
        }

        val taskRVVBListAdapter = TaskRVVBListAdapter(this,isListMutableLiveData) { type, position, task ->
            if (type == "delete") {
                //deletes task
                taskViewModal.deleteTaskUsingID(task.id)
                //restore Deleted task
                restoreDeletedTask(task)
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
                        hideKeyBoard(it)
                        updateTaskDialog.dismiss()
                        taskViewModal
                            .updateParticularTaskField(
                                task.id,
                                updateTaskLevel.text.toString().trim(),
                                updateETTitle.text.toString().trim(),
                                updateETDesc.text.toString().trim(),
                            )

                    }
                }
                updateTaskDialog.show()
            }
        }
        mainBinding.taskRV.adapter = taskRVVBListAdapter
        ViewCompat.setNestedScrollingEnabled(mainBinding.taskRV,false)

        taskRVVBListAdapter.registerAdapterDataObserver(object:RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                mainBinding.nestedScrollView.smoothScrollTo(0,positionStart)
            }
        })
        callGetTaskList(taskRVVBListAdapter)
        callSortByLiveData()
        statusCallBack()
        callSearch()
        callSortByDialog()
    }

    private fun statusCallBack() {
        taskViewModal.statusLiveData.observe(this){
            when (it.status) {
                Status.LOADING -> {
                    loadingDialog.show()
                }

                Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    when(it.data as StatusResult){
                        Added->{
                            Log.d("StatusResult","inserted")
                        }
                        Deleted->{
                            Log.d("StatusResult","deleted")
                        }
                        Updated->{
                            Log.d("StatusResult","updated")
                        }
                    }
                    it.message?.let { it1 -> LongToastShow(it1) }
                }

                Status.ERROR -> {
                    loadingDialog.dismiss()
                    it.message?.let { it1 -> LongToastShow(it1) }
                }
            }
        }
    }

    private fun restoreDeletedTask(deletedTask:Task){
        val snackbar=Snackbar.make(
            mainBinding.root,"Deleted ${deletedTask.title}",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo"){
            taskViewModal.insertTask(deletedTask)
        }
        snackbar.show()

    }

    private fun callSearch() {
        mainBinding.edSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(query: Editable) {
                if (query.toString().isNotEmpty()){
                    taskViewModal.searchTaskList(query.toString())
                }else{
                    callSortByLiveData()
                }
            }
        })

        mainBinding.edSearch.setOnEditorActionListener{ v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                hideKeyBoard(v)
                return@setOnEditorActionListener true
            }
            false
        }

        callSortByDialog()
    }

    private fun callSortByLiveData(){
        taskViewModal.sortByLiveData.observe(this){
            taskViewModal.getTaskList(it.second,it.first)
        }
    }

    private fun callSortByDialog() {
        var checkedItem = 0   // 2 is default item set
        val items = arrayOf("Title Ascending", "Title Descending","Date Ascending","Date Descending","High","Medium")

        mainBinding.sortImg.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Sort By")
                .setPositiveButton("Ok") { _, _ ->
                    when (checkedItem) {
                        0 -> {
                            taskViewModal.setSortedBy(Pair("title",true))
                        }
                        1 -> {
                            taskViewModal.setSortedBy(Pair("title",false))
                        }
                        2 -> {
                            taskViewModal.setSortedBy(Pair("date",true))
                        }
                        3 -> {
                            taskViewModal.setSortedBy(Pair("date",false))
                        }
                        4 -> {
                            taskViewModal.setSortedBy(Pair("level",false))
                        }
                        else->{
                            taskViewModal.setSortedBy(Pair("level",true))
                        }
                    }
                }
                .setSingleChoiceItems(items, checkedItem) { _, selectedItemIndex ->
                    checkedItem = selectedItemIndex
                }
                .setCancelable(false)
                .show()
        }
    }


    private fun callGetTaskList(taskRecyclerViewAdapter: TaskRVVBListAdapter) {
        CoroutineScope(Dispatchers.Main).launch {
            taskViewModal.taskStateFlow.collectLatest {
                when (it.status) {
                    Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Status.SUCCESS -> {
                        it.data?.collect { taskList ->
                            loadingDialog.dismiss()
                            taskRecyclerViewAdapter.submitList(taskList)
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