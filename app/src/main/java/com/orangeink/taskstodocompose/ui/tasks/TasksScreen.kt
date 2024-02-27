package com.orangeink.taskstodocompose.ui.tasks

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orangeink.taskstodocompose.R
import com.orangeink.taskstodocompose.data.db.entity.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksScreen(
    tasksState: TasksState,
    onSortClick: (() -> Unit)? = null,
    onSaveTask: ((Task) -> Unit)? = null,
    onUpdateTask: ((Task) -> Unit)? = null,
    onDeleteCompletedTasks: (() -> Unit)? = null
) {
    val lazyColumnState = rememberLazyListState()
    var showSheet by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 32.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tasks",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onSortClick?.invoke() }) {
                    Icon(
                        painter = painterResource(
                            id = when (tasksState.sortType) {
                                SortType.Date -> R.drawable.round_calendar_month_24
                                SortType.Completion -> R.drawable.round_check_circle_24
                            }
                        ),
                        contentDescription = "sort-tasks"
                    )
                }
                if (tasksState.tasks.any { it.completed }) {
                    IconButton(onClick = { onDeleteCompletedTasks?.invoke() }) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.round_delete_outline_24
                            ),
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = "sort-tasks"
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }) {
                Icon(
                    painter = painterResource(R.drawable.round_add_24),
                    contentDescription = "add-task"
                )
            }
        }
    ) {
        if (showSheet) {
            TaskBottomSheet(
                selectedTask,
                onDismiss = {
                    showSheet = false
                    selectedTask = null
                },
                onSaveTask = onSaveTask,
                onUpdateTask = onUpdateTask
            )
        }
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(vertical = 8.dp),
            state = lazyColumnState,
        ) {
            items(
                count = tasksState.tasks.size,
                key = { index ->
                    tasksState.tasks[index]
                }
            ) { index ->
                TaskItem(
                    modifier = Modifier
                        .animateItemPlacement()
                        .animateContentSize(),
                    task = tasksState.tasks[index],
                    onTaskCompleted = onUpdateTask
                ) { task ->
                    selectedTask = task
                    showSheet = true
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onTaskCompleted: ((Task) -> Unit)? = null,
    onTaskUpdate: ((Task) -> Unit)? = null
) {
    var completed by remember { mutableStateOf(task.completed) }
    Row(
        modifier = modifier
            .clickable { onTaskUpdate?.invoke(task) }
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Checkbox(checked = completed, onCheckedChange = {
                completed = it
                onTaskCompleted?.invoke(task.copy(completed = it))
            })
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = task.title,
                fontSize = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            task.dueDate?.let { date ->
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .format(date)
                        .uppercase(),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    TasksScreen(
        TasksState(
            listOf(
                Task("Pay all the electric bills", Date(), id = 0),
                Task("Complete all college assignments", id = 1),
                Task("Bake Bread", id = 2),
                Task("Give Clothes to charity", id = 3),
                Task("Watch movies", Date(), id = 4)
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    TaskItem(task = Task("Pay all the electric bills", Date()))
}