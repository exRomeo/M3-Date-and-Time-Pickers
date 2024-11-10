package com.example.datetimepickers

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.datetimepickers.date_picker_diaog.DateConstraints
import com.example.datetimepickers.date_picker_diaog.DatePickerDialog
import com.example.datetimepickers.ui.theme.DateTimePickersTheme
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DecimalStyle
import org.threeten.bp.format.TextStyle
import java.util.Locale


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("TAG", DayOfWeek.FRIDAY.getDisplayName(TextStyle.NARROW, Locale("ar")))
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DateTimePickersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreenContent(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}


@Composable
fun MainScreenContent(modifier: Modifier = Modifier) {
    val showTimePicker = rememberSaveable { mutableStateOf(false) }
    val showDatePicker = rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showTimePicker.value = !showTimePicker.value }) {
            Text("Show Time Picker")
        }
        Button(onClick = { showDatePicker.value = !showDatePicker.value }) {
            Text("show date picker")
        }
    }

    TimePickerDialog(
        visible = showTimePicker,
        initialTime = LocalTime.now(),
        onConfirmed = {
            val formatter = DateTimeFormatter.ofPattern("hh:mm a").withDecimalStyle(
                DecimalStyle.of(Locale.getDefault())
            )
            val formattedTime = it.format(formatter)
            Log.i("TAG", "MainScreenContent: $formattedTime")
            showTimePicker.value = false
        }, onDismissed = {
            showTimePicker.value = false
        })

    DatePickerDialog(
        visible = showDatePicker,
        onDismissed = {
            showDatePicker.value = false
        },
        onConfirmed = {
            val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy").withDecimalStyle(
                DecimalStyle.of(Locale.getDefault())
            )
            val formattedTime = it.format(formatter)
            Log.i("TAG", "MainScreenContent: $formattedTime")
            showDatePicker.value = false
        },
        initialDate = LocalDate.now(),
        dateConstraints = DateConstraints.AllDates
    )
}
