package com.example.todoapp.viewmodals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.models.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.utils.Resource

class TaskViewModal(application: Application):AndroidViewModel(application) {

    private val taskRepository=TaskRepository(application)

    fun getTaskList()=taskRepository.getTaskList()

    fun insertTask(task:Task):MutableLiveData<Resource<Long>>{
        return taskRepository.insertTask(task)
    }

    fun deleteTaskUsingID(taskId:String):MutableLiveData<Resource<Int>>{
        return taskRepository.deleteTaskUsingID(taskId)
    }

    fun updateParticularTaskField(taskId:String,title:String,description:String):MutableLiveData<Resource<Int>>{
        return taskRepository.updateTaskParticularField(taskId,title,description)
    }



}