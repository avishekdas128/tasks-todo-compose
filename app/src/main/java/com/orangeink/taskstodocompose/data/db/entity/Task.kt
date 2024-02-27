package com.orangeink.taskstodocompose.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity(tableName = "tasks")
@Parcelize
data class Task(
    val title: String,
    val dueDate: Date? = null,
    val completed: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable
