package com.example.todoapp.dao

import androidx.room.*
import com.example.todoapp.models.Task

@Dao
interface TaskDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long
}