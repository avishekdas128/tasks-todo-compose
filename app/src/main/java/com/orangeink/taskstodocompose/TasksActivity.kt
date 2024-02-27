package com.orangeink.taskstodocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.orangeink.taskstodocompose.ui.tasks.TasksScreen
import com.orangeink.taskstodocompose.ui.theme.TasksToDoComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TasksToDoComposeTheme {
                val taskViewModel: TasksViewModel = hiltViewModel()
                val allTasks by taskViewModel.allTasks.collectAsState()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TasksScreen(
                        allTasks,
                        onSortClick = taskViewModel::getData,
                        onSaveTask = taskViewModel::addTask,
                        onUpdateTask = taskViewModel::updateTask,
                        onDeleteCompletedTasks = taskViewModel::deleteCompletedTasks
                    )
                }
            }
        }
    }
}