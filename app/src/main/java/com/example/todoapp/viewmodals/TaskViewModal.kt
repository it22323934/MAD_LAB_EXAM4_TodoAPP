package com.example.todoapp.viewmodals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Query
import com.example.todoapp.models.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.utils.Resource

class TaskViewModal(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application)
    val taskStateFlow get() = taskRepository.taskStateFlow
    val statusLiveData get() = taskRepository.statusLiveData


    fun getTaskList() {
        taskRepository.getTaskList()
    }

    fun insertTask(task: Task){
        taskRepository.insertTask(task)
    }

    fun deleteTaskUsingID(taskId: String) {
        taskRepository.deleteTaskUsingID(taskId)
    }

    fun updateParticularTaskField(
        taskId: String,
        level: String,
        title: String,
        description: String
    ) {
        taskRepository.updateTaskParticularField(taskId, level, title, description)
    }

    fun searchTaskList(query: String) {
        taskRepository.searchTaskList(query)
    }


}