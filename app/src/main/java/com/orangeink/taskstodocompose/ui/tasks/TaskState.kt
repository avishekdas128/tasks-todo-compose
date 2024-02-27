package com.orangeink.taskstodocompose.ui.tasks

import com.orangeink.taskstodocompose.data.db.entity.Task

data class TasksState(
    val tasks: List<Task> = emptyList(),
    val sortType: SortType = SortType.Date
)

enum class SortType {
    Date, Completion
}
