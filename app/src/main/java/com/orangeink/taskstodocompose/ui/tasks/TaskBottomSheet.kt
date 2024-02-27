package com.orangeink.taskstodocompose.ui.tasks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orangeink.taskstodocompose.R
import com.orangeink.taskstodocompose.data.db.entity.Task
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskBottomSheet(
    task: Task? = null,
    onDismiss: () -> Unit,
    onSaveTask: ((Task) -> Unit)? = null,
    onUpdateTask: ((Task) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState()
    val focusRequester = remember { FocusRequester() }
    val textFieldState = rememberTextFieldState(initialText = task?.title ?: "")
    var openDatePicker by remember { mutableStateOf(false) }
    var dueDate by remember { mutableStateOf(task?.dueDate) }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        dragHandle = null,
        contentColor = MaterialTheme.colorScheme.onSurface,
        windowInsets = WindowInsets.ime
    ) {
        var completed by remember { mutableStateOf(task?.completed ?: false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp)
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(checked = completed, onCheckedChange = {
                    completed = it
                })
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.Center) {
                BasicTextField2(
                    state = textFieldState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    decorator = { innerTextField ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (textFieldState.text.isEmpty()) {
                                Text(
                                    text = "Eg. pay all bills",
                                    color = Color.Gray,
                                    fontSize = 18.sp
                                )
                            }
                        }
                        innerTextField()
                    }
                )
            }
        }
        HorizontalDivider(
            thickness = Dp.Hairline,
            color = Color.Gray,
            modifier = Modifier.padding(top = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = dueDate?.let {
                    SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).format(it)
                } ?: "Due",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier
                    .border(
                        width = 0.8.dp,
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Gray
                    )
                    .clickable { openDatePicker = true }
                    .padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    task?.let {
                        onUpdateTask?.invoke(
                            Task(
                                textFieldState.text.toString(),
                                dueDate,
                                completed,
                                id = it.id
                            )
                        )
                    } ?: onSaveTask?.invoke(
                        Task(
                            textFieldState.text.toString(),
                            dueDate,
                            completed
                        )
                    )
                    scope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
                        if (!modalBottomSheetState.isVisible) {
                            onDismiss.invoke()
                        }
                    }
                },
                enabled = textFieldState.text.isNotEmpty()
            ) {
                Icon(
                    painter = painterResource(id = task?.let { R.drawable.round_save_24 }
                        ?: R.drawable.round_add_circle_24),
                    contentDescription = "save-task"
                )
            }
        }
    }

    DueDatePicker(openDatePicker = openDatePicker) { selectedDate ->
        dueDate = selectedDate?.let { Date(it) }
        openDatePicker = false
    }

    LaunchedEffect(modalBottomSheetState.isVisible) {
        focusRequester.requestFocus()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueDatePicker(openDatePicker: Boolean = false, onDismiss: (selectedDate: Long?) -> Unit) {
    val datePickerState = rememberDatePickerState()
    if (openDatePicker) {
        DatePickerDialog(
            onDismissRequest = { onDismiss(datePickerState.selectedDateMillis) },
            confirmButton = {
                TextButton(onClick = { onDismiss(datePickerState.selectedDateMillis) }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss(datePickerState.selectedDateMillis) }) {
                    Text("CANCEL")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}