package com.example.datetimepickers.date_picker_diaog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.datetimepickers.R
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset


/**
 * [DatePickerDialog] is a composable that displays a dialog with a date picker
 * @param visible the state that controls the visibility of the dialog
 * @param initialDate the initial date to be displayed in the date picker
 * @param dateConstraints the constraints to be applied to the selectable dates
 * @param onConfirmed the callback to be invoked when the user confirms the selected date
 * @param onDismissed the callback to be invoked when the user dismisses the dialog
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    visible: State<Boolean>,
    initialDate: LocalDate,
    dateConstraints: DateConstraints = DateConstraints.AllDates,
    onConfirmed: (LocalDate) -> Unit,
    onDismissed: () -> Unit
) {

    if (visible.value) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDate.atStartOfDay(
                ZoneId.ofOffset(
                    "UTC",
                    ZoneOffset.UTC
                )
            )
                .toInstant().toEpochMilli(),
            selectableDates = dateConstraints
        )
        val isConfirmEnabled by remember {
            derivedStateOf {
                datePickerState.selectedDateMillis?.let {
                    datePickerState.selectableDates
                        .isSelectableDate(it)
                } ?: false
            }
        }
        DatePickerDialog(
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
                            Instant.ofEpochMilli(datePickerState.selectedDateMillis ?: 0)
                                .atZone(ZoneId.systemDefault()).toLocalDate()
                        )
                    },
                    enabled = isConfirmEnabled
                ) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = if (isConfirmEnabled) 1f else 0.5f),
                            fontSize = 16.sp,
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissed) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            16.sp,
                        )
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                dateFormatter = remember { DatePickerDefaults.dateFormatter() },
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    headlineContentColor = MaterialTheme.colorScheme.primary,
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    disabledSelectedDayContainerColor = MaterialTheme.colorScheme.primary.copy(0.5f),
                    todayDateBorderColor = MaterialTheme.colorScheme.primary,
                    todayContentColor = MaterialTheme.colorScheme.primary,
                    selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                    currentYearContentColor = MaterialTheme.colorScheme.primary,
                    weekdayContentColor = MaterialTheme.colorScheme.primary,
                    navigationContentColor = MaterialTheme.colorScheme.onSurface,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    )
                )
            )
        }
    }
}