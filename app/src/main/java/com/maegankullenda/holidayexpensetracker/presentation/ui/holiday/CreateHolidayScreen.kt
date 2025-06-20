package com.maegankullenda.holidayexpensetracker.presentation.ui.holiday

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

    // Forest/Jungle theme gradient background
    val forestGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2E5A27), // Deep forest green
            Color(0xFF3A7D32), // Rich green
            Color(0xFF4A7C59), // Moss green
            Color(0xFF5D8A66), // Sage green
            Color(0xFF6B8E23)  // Olive green
        )
    )

    LaunchedEffect(state.isCreated) {
        if (state.isCreated) {
            onHolidayCreated()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Create Holiday",
                        color = Color(0xFFF5F5DC), // Beige (forest cream)
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFF5F5DC) // Beige (forest cream)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(forestGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5DC) // Beige (forest cream)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = state.name,
                            onValueChange = { viewModel.onEvent(CreateHolidayEvent.OnNameChange(it)) },
                            label = { 
                                Text(
                                    "Holiday Name",
                                    color = Color(0xFF2E5A27) // Deep forest green
                                ) 
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4A7C59), // Moss green
                                unfocusedBorderColor = Color(0xFF2E5A27), // Deep forest green
                                focusedLabelColor = Color(0xFF2E5A27), // Deep forest green
                                unfocusedLabelColor = Color(0xFF2E5A27) // Deep forest green
                            )
                        )

                        OutlinedTextField(
                            value = state.destination,
                            onValueChange = { viewModel.onEvent(CreateHolidayEvent.OnDestinationChange(it)) },
                            label = { 
                                Text(
                                    "Destination",
                                    color = Color(0xFF2E5A27) // Deep forest green
                                ) 
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4A7C59), // Moss green
                                unfocusedBorderColor = Color(0xFF2E5A27), // Deep forest green
                                focusedLabelColor = Color(0xFF2E5A27), // Deep forest green
                                unfocusedLabelColor = Color(0xFF2E5A27) // Deep forest green
                            )
                        )

                        OutlinedTextField(
                            value = state.totalBudget,
                            onValueChange = { viewModel.onEvent(CreateHolidayEvent.OnTotalBudgetChange(it)) },
                            label = { 
                                Text(
                                    "Total Budget",
                                    color = Color(0xFF2E5A27) // Deep forest green
                                ) 
                            },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Decimal
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4A7C59), // Moss green
                                unfocusedBorderColor = Color(0xFF2E5A27), // Deep forest green
                                focusedLabelColor = Color(0xFF2E5A27), // Deep forest green
                                unfocusedLabelColor = Color(0xFF2E5A27) // Deep forest green
                            )
                        )

                        ExposedDropdownMenuBox(
                            expanded = state.isCurrencyDropdownExpanded,
                            onExpandedChange = { viewModel.onEvent(CreateHolidayEvent.OnCurrencyDropdownExpandedChange(it)) }
                        ) {
                            OutlinedTextField(
                                value = state.selectedCurrency.name,
                                onValueChange = {},
                                readOnly = true,
                                label = { 
                                    Text(
                                        "Currency",
                                        color = Color(0xFF2E5A27) // Deep forest green
                                    ) 
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isCurrencyDropdownExpanded) },
                                modifier = Modifier.fillMaxWidth().menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4A7C59), // Moss green
                                    unfocusedBorderColor = Color(0xFF2E5A27), // Deep forest green
                                    focusedLabelColor = Color(0xFF2E5A27), // Deep forest green
                                    unfocusedLabelColor = Color(0xFF2E5A27) // Deep forest green
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = state.isCurrencyDropdownExpanded,
                                onDismissRequest = { viewModel.onEvent(CreateHolidayEvent.OnCurrencyDropdownExpandedChange(false)) }
                            ) {
                                Currency.values().forEach { currency ->
                                    DropdownMenuItem(
                                        text = { 
                                            Text(
                                                "${currency.name} (${currency.symbol})",
                                                color = Color(0xFF2E5A27) // Deep forest green
                                            ) 
                                        },
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
                                Text(
                                    "Start Date",
                                    color = Color(0xFF2E5A27), // Deep forest green
                                    fontWeight = FontWeight.Bold
                                )
                                Button(
                                    onClick = { viewModel.onEvent(CreateHolidayEvent.OnStartDateClick) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4A7C59) // Moss green
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        state.startDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                                        color = Color(0xFFF5F5DC) // Beige (forest cream)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "End Date",
                                    color = Color(0xFF2E5A27), // Deep forest green
                                    fontWeight = FontWeight.Bold
                                )
                                Button(
                                    onClick = { viewModel.onEvent(CreateHolidayEvent.OnEndDateClick) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4A7C59) // Moss green
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        state.endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                                        color = Color(0xFFF5F5DC) // Beige (forest cream)
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = { viewModel.onEvent(CreateHolidayEvent.OnSubmit) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2E5A27) // Deep forest green
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Create Holiday",
                                color = Color(0xFFF5F5DC), // Beige (forest cream)
                                fontWeight = FontWeight.Bold
                            )
                        }

                        state.error?.let { error ->
                            Text(
                                text = error,
                                color = Color(0xFF8B4513), // Saddle brown (forest error)
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
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
                        Text(
                            "OK",
                            color = Color(0xFF2E5A27) // Deep forest green
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onEvent(CreateHolidayEvent.OnDatePickerDismiss) }) {
                        Text(
                            "Cancel",
                            color = Color(0xFF8B4513) // Saddle brown
                        )
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }
    }
} 