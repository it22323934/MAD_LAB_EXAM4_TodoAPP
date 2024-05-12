package com.example.todoapp.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.models.Task
import com.example.todoapp.utils.Resource
import com.example.todoapp.utils.Resource.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TaskRepository(application: Application) {

    private val taskDao= TaskDatabase.getInstance(application).taskDao


    fun getTaskList()= flow{
        emit(Loading())
        try {
            val result =taskDao.getTaskList()
            emit(Success(result))

        }catch (e:Exception){
            emit(Error(e.message.toString()))
        }
    }

    fun insertTask(task:Task)=MutableLiveData<Resource<Long>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result=taskDao.insertTask(task)
                postValue(Success(result))
            }
        }catch (e:Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun deleteTaskUsingID(taskId:String)=MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result=taskDao.deleteTaskUsingId(taskId)
                postValue(Success(result))
            }
        }catch (e:Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun updateTaskParticularField(taskId:String,level:String,title:String,description:String)=MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result=taskDao.updateTaskParticularFiled(taskId,level,title,description)
                postValue(Success(result))
            }
        }catch (e:Exception){
            postValue(Error(e.message.toString()))
        }
    }

}