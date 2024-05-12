package com.example.todoapp.viewmodals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.models.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.utils.Resource

class TaskViewModal(application: Application):AndroidViewModel(application) {

    private val taskRepository=TaskRepository(application)

    fun insertTask(task:Task):MutableLiveData<Resource<Long>>{
        return taskRepository.insertTask(task)
    }

}