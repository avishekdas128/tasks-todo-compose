package com.orangeink.taskstodocompose.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.orangeink.taskstodocompose.data.db.converter.Converters
import com.orangeink.taskstodocompose.data.db.dao.TaskDao
import com.orangeink.taskstodocompose.data.db.entity.Task


@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}