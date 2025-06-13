package com.maegankullenda.holidayexpensetracker.presentation.ui.holiday

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.Instant
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.SelectableDates

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHolidayScreen(
    onNavigateBack: () -> Unit,
    onHolidayCreated: () -> Unit,
    viewModel: CreateHolidayViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isCreated) {
        if (state.isCreated) {
            onHolidayCreated()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Holiday") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.onEvent(CreateHolidayEvent.OnNameChange(it)) },
                label = { Text("Holiday Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.destination,
                onValueChange = { viewModel.onEvent(CreateHolidayEvent.OnDestinationChange(it)) },
                label = { Text("Destination") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.totalBudget,
                onValueChange = { viewModel.onEvent(CreateHolidayEvent.OnTotalBudgetChange(it)) },
                label = { Text("Total Budget") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = state.isCurrencyDropdownExpanded,
                onExpandedChange = { viewModel.onEvent(CreateHolidayEvent.OnCurrencyDropdownExpandedChange(it)) }
            ) {
                OutlinedTextField(
                    value = state.selectedCurrency.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Currency") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isCurrencyDropdownExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = state.isCurrencyDropdownExpanded,
                    onDismissRequest = { viewModel.onEvent(CreateHolidayEvent.OnCurrencyDropdownExpandedChange(false)) }
                ) {
                    Currency.values().forEach { currency ->
                        DropdownMenuItem(
                            text = { Text("${currency.name} (${currency.symbol})") },
                            onClick = {
                                viewModel.onEvent(CreateHolidayEvent.OnCurrencySelect(currency))
                                viewModel.onEvent(CreateHolidayEvent.OnCurrencyDropdownExpandedChange(false))
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Start Date")
                    Button(
                        onClick = { viewModel.onEvent(CreateHolidayEvent.OnStartDateClick) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(state.startDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("End Date")
                    Button(
                        onClick = { viewModel.onEvent(CreateHolidayEvent.OnEndDateClick) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(state.endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
                    }
                }
            }

            if (state.showDatePicker) {
                val todayMillis = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = if (state.isStartDateSelection) {
                        state.startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    } else {
                        state.endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    },
                    initialDisplayedMonthMillis = todayMillis,
                    selectableDates = object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                            return utcTimeMillis >= todayMillis
                        }
                        override fun isSelectableYear(year: Int): Boolean = true
                    }
                )

                DatePickerDialog(
                    onDismissRequest = { viewModel.onEvent(CreateHolidayEvent.OnDatePickerDismiss) },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val selectedDate = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                viewModel.onEvent(CreateHolidayEvent.OnDateSelected(selectedDate))
                            }
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.onEvent(CreateHolidayEvent.OnDatePickerDismiss) }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState
                    )
                }
            }

            Button(
                onClick = { viewModel.onEvent(CreateHolidayEvent.OnSubmit) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Holiday")
            }

            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
} 