package com.example.todoapp.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.models.Task
import com.example.todoapp.utils.Resource
import com.example.todoapp.utils.Resource.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskRepository(application: Application) {

    private val taskDao= TaskDatabase.getInstance(application).taskDao

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
}