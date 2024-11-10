package com.example.datetimepickers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.temporal.ChronoUnit

/**
 * [TimePickerDialog] is a composable that displays a dialog with a time picker
 * @param visible the state that controls the visibility of the dialog
 * @param initialTime the initial time to be displayed in the time picker
 * @param selectedDate the selected date to be used to validate the selected time can be null to disable validation
 * @param onConfirmed the callback to be invoked when the user confirms the selected time
 * @param onDismissed the callback to be invoked when the user dismisses the dialog
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    visible: State<Boolean>,
    initialTime: LocalTime,
    selectedDate: State<LocalDate?> = mutableStateOf(null),
    onConfirmed: (LocalTime) -> Unit,
    onDismissed: () -> Unit
) {

    if (visible.value) {
        val timePickerState = rememberTimePickerState(
            initialHour = initialTime.hour,
            initialMinute = initialTime.minute,
        )

        val isValidTime by remember {
            derivedStateOf {
                selectedDate.value?.let { localDate ->
                    (LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                        .plusMinutes(15) <= LocalDateTime.of(
                        localDate,
                        LocalTime.of(timePickerState.hour, timePickerState.minute)
                    ))
                } ?: true
            }
        }

        val confirmationButtonTextColor: @Composable () -> Color = remember {
            {
                MaterialTheme.colorScheme.primary.copy(alpha = if (isValidTime) 1f else 0.5f)
            }
        }

        val selectorColor: @Composable () -> Color = remember {
            {
                if (isValidTime) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            }
        }

        androidx.compose.material3.DatePickerDialog(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
            shape = MaterialTheme.shapes.large,
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = onDismissed,
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmed(
                            LocalTime.of(timePickerState.hour, timePickerState.minute)
                        )
                    },
                    enabled = isValidTime
                ) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = confirmationButtonTextColor(),
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissed) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            },
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .padding(
                            bottom = 16.dp,
                            start = 24.dp
                        ),
                    text = stringResource(id = R.string.select_time),
                    style = MaterialTheme.typography.bodyMedium
                )
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background,
                        clockDialUnselectedContentColor = MaterialTheme.colorScheme.primary,
                        clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        clockDialColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
                        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                        periodSelectorBorderColor = MaterialTheme.colorScheme.primary,
                        periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        selectorColor = selectorColor(),
                        timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.20f
                        ),
                    )
                )
                AnimatedVisibility(visible = !isValidTime) {
                    Text(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .fillMaxWidth(),
                        text = stringResource(id = R.string.please_enter_time_in_future),
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.error)
                    )
                }
            }
        }
    }
}