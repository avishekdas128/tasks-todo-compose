package com.orangeink.taskstodocompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeink.taskstodocompose.data.db.entity.Task
import com.orangeink.taskstodocompose.domain.TasksRepository
import com.orangeink.taskstodocompose.ui.tasks.SortType
import com.orangeink.taskstodocompose.ui.tasks.TasksState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _allTasks = MutableStateFlow(TasksState())
    val allTasks = _allTasks.asStateFlow()

    private var sortType: SortType = SortType.Date
    private var dataJob: Job? = null

    init {
        getData(isInitialCall = true)
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.addTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.updateTask(task)
        }
    }

    fun deleteCompletedTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.deleteCompletedTasks()
        }
    }

    fun getData(isInitialCall: Boolean = false) {
        viewModelScope.launch {
            if (isInitialCall.not()) {
                val index = SortType.entries.indexOf(sortType)
                val nextIndex = (index + 1) % SortType.entries.size
                sortType = SortType.entries[nextIndex]
                dataJob?.cancel()
            }
            when (sortType) {
                SortType.Date -> dataJob = tasksRepository.getAllTaskByDate().cancellable()
                    .onEach { allTasks ->
                        _allTasks.update {
                            it.copy(
                                tasks = allTasks,
                                sortType = sortType
                            )
                        }
                    }.launchIn(this)

                SortType.Completion -> dataJob =
                    tasksRepository.getAllTaskByCompletion().cancellable()
                        .onEach { allTasks ->
                            _allTasks.update {
                                it.copy(
                                    tasks = allTasks,
                                    sortType = sortType
                                )
                            }
                        }.launchIn(this)
            }
        }
    }

}