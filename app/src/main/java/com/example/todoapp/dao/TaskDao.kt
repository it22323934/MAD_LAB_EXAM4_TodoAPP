package com.example.todoapp.dao

import android.icu.text.CaseMap.Title
import androidx.room.*
import com.example.todoapp.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task ORDER BY date DESC")
    fun getTaskList() : Flow<List<Task>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long


    @Delete
    suspend fun deleteTask(task:Task):Int

    @Query("DELETE FROM Task WHERE taskId== :taskId")
    suspend fun deleteTaskUsingId(taskId:String):Int

    @Query("UPDATE Task SET level=:level,taskTitle=:title,description=:description WHERE taskId=:taskId")
    suspend fun updateTaskParticularFiled(taskId:String,level:String,title:String,description:String):Int

    @Query("SELECT * FROM Task WHERE taskTitle LIKE :query ORDER BY date DESC")
    fun searchTaskList(query: String) : Flow<List<Task>>

}