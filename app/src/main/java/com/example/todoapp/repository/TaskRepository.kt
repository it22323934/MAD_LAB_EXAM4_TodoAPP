package com.example.todoapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.models.Task
import com.example.todoapp.utils.Resource
import com.example.todoapp.utils.Resource.*
import com.example.todoapp.utils.StatusResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TaskRepository(application: Application) {

    private val taskDao= TaskDatabase.getInstance(application).taskDao


    private val _taskStateFlow=MutableStateFlow<Resource<Flow<List<Task>>>>(Loading())
    val taskStateFlow:StateFlow<Resource<Flow<List<Task>>>>
        get()=_taskStateFlow

    private val _statusLiveData=MutableLiveData<Resource<StatusResult>>()
    val statusLiveData:LiveData<Resource<StatusResult>>
        get()=_statusLiveData

    private val _sortByLiveData=MutableLiveData<Pair<String,Boolean>>().apply {
        postValue(Pair("title",true))
    }
    val sortByLiveData:LiveData<Pair<String,Boolean>>
        get()= _sortByLiveData

    fun setSortedBy(sort:Pair<String,Boolean>){
        _sortByLiveData.postValue(sort)
    }
    fun getTaskList(isAsc:Boolean,sortByName: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _taskStateFlow.emit(Loading())
                delay(500)
                val result=if(sortByName=="title"){
                    taskDao.getTaskListSortByTaskTitle(isAsc)
                }else if (sortByName=="date"){
                    taskDao.getTaskListSortByTaskDate(isAsc)
                } else {
                    taskDao.getTaskListSortByTaskPriority(isAsc)
                }
                _taskStateFlow.emit(Success("loading", result))

            } catch (e: Exception) {
                _taskStateFlow.emit(Error(e.message.toString()))
            }
        }
    }

    fun insertTask(task:Task){
        try {
            _statusLiveData.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result=taskDao.insertTask(task)
                handleResult(result.toInt(),"Inserted Task Successfully",StatusResult.Added)
            }
        }catch (e:Exception){
            _statusLiveData.postValue(Error(e.message.toString()))
        }
    }

    fun deleteTaskUsingID(taskId:String){
        try {
            _statusLiveData.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result=taskDao.deleteTaskUsingId(taskId)
                handleResult(result,"Deleted Task Successfully",StatusResult.Deleted)
            }
        }catch (e:Exception){
            _statusLiveData.postValue(Error(e.message.toString()))
        }
    }

    fun updateTaskParticularField(taskId:String,level:String,title:String,description:String){
        try {
            _statusLiveData.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result=taskDao.updateTaskParticularFiled(taskId,level,title,description)
                handleResult(result,"Updated Task Successfully",StatusResult.Updated)
            }
        }catch (e:Exception){
            _statusLiveData.postValue(Error(e.message.toString()))
        }
    }

    fun searchTaskList(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _taskStateFlow.emit(Loading())
                val result = taskDao.searchTaskList("%${query}%")
                _taskStateFlow.emit(Success("loading", result))
            } catch (e: Exception) {
                _taskStateFlow.emit(Error(e.message.toString()))
            }
        }
    }

    private fun handleResult(result:Int,message:String,statusResult:StatusResult){
        if(result!=-1){
            _statusLiveData.postValue(Success(message,statusResult))
        }else{
            _statusLiveData.postValue(Error("Something went wrong",statusResult))
        }
    }

}