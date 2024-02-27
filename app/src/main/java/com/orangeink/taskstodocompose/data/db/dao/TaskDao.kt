package com.orangeink.taskstodocompose.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.orangeink.taskstodocompose.data.db.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY dueDate DESC")
    fun getTasksByDate(): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY completed DESC")
    fun getTasksByCompletion(): Flow<List<Task>>

    @Query("DELETE FROM tasks WHERE completed")
    suspend fun deleteCompletedTasks()
}