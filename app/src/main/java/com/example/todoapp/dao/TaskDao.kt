package com.example.todoapp.dao

import android.icu.text.CaseMap.Title
import androidx.room.*
import com.example.todoapp.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("""SELECT * FROM Task ORDER BY CASE WHEN :isAsc=1 THEN taskTitle END ASC,CASE WHEN :isAsc=0 THEN taskTitle END DESC""")
    fun getTaskListSortByTaskTitle(isAsc: Boolean): Flow<List<Task>>

    @Query("""SELECT * FROM Task ORDER BY CASE WHEN :isAsc=1 THEN date END ASC,CASE WHEN :isAsc=0 THEN date END DESC""")
    fun getTaskListSortByTaskDate(isAsc: Boolean): Flow<List<Task>>

    @Query("""
    SELECT * FROM Task 
    WHERE 
        CASE 
            WHEN :isAsc = 0 THEN
                level = 'high'
            ELSE
                level = 'medium'
        END
""")
    fun getTaskListSortByTaskPriority(isAsc: Boolean): Flow<List<Task>>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long


    @Delete
    suspend fun deleteTask(task: Task): Int

    @Query("DELETE FROM Task WHERE taskId== :taskId")
    suspend fun deleteTaskUsingId(taskId: String): Int

    @Query("UPDATE Task SET level=:level,taskTitle=:title,description=:description WHERE taskId=:taskId")
    suspend fun updateTaskParticularFiled(
        taskId: String,
        level: String,
        title: String,
        description: String
    ): Int

    @Query("SELECT * FROM Task WHERE taskTitle LIKE :query ORDER BY date DESC")
    fun searchTaskList(query: String): Flow<List<Task>>


}