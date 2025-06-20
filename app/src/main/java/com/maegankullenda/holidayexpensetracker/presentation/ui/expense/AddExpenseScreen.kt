package com.maegankullenda.holidayexpensetracker.presentation.ui.expense

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
import com.maegankullenda.holidayexpensetracker.domain.model.ExpenseCategory
import androidx.compose.foundation.text.KeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddExpenseViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Spanish theme gradient background
    val spanishGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF8B0000), // Dark red (Spanish flag)
            Color(0xFFDC143C), // Crimson red (flamenco)
            Color(0xFFFF8C00), // Dark orange (Mediterranean sunset)
            Color(0xFFFFD700), // Gold (Spanish gold)
            Color(0xFFDAA520)  // Goldenrod (warm Spanish earth)
        )
    )

    LaunchedEffect(state.isExpenseAdded) {
        if (state.isExpenseAdded) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Add Expense",
                        color = Color(0xFFFFF8DC), // Cornsilk (Spanish cream)
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back",
                            tint = Color(0xFFFFF8DC) // Cornsilk (Spanish cream)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(spanishGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF8DC) // Cornsilk (Spanish cream)
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
                            value = state.amount,
                            onValueChange = { viewModel.onEvent(AddExpenseEvent.OnAmountChange(it)) },
                            label = { 
                                Text(
                                    "Amount",
                                    color = Color(0xFF8B0000) // Dark red
                                ) 
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFDC143C), // Crimson red
                                unfocusedBorderColor = Color(0xFF8B0000), // Dark red
                                focusedLabelColor = Color(0xFF8B0000), // Dark red
                                unfocusedLabelColor = Color(0xFF8B0000) // Dark red
                            )
                        )

                        OutlinedTextField(
                            value = state.description,
                            onValueChange = { viewModel.onEvent(AddExpenseEvent.OnDescriptionChange(it)) },
                            label = { 
                                Text(
                                    "Description",
                                    color = Color(0xFF8B0000) // Dark red
                                ) 
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFDC143C), // Crimson red
                                unfocusedBorderColor = Color(0xFF8B0000), // Dark red
                                focusedLabelColor = Color(0xFF8B0000), // Dark red
                                unfocusedLabelColor = Color(0xFF8B0000) // Dark red
                            )
                        )

                        ExposedDropdownMenuBox(
                            expanded = state.isCategoryDropdownExpanded,
                            onExpandedChange = { viewModel.onEvent(AddExpenseEvent.OnCategoryDropdownExpandedChange(it)) }
                        ) {
                            OutlinedTextField(
                                value = state.selectedCategory.name,
                                onValueChange = {},
                                readOnly = true,
                                label = { 
                                    Text(
                                        "Category",
                                        color = Color(0xFF8B0000) // Dark red
                                    ) 
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isCategoryDropdownExpanded) },
                                modifier = Modifier.fillMaxWidth().menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFDC143C), // Crimson red
                                    unfocusedBorderColor = Color(0xFF8B0000), // Dark red
                                    focusedLabelColor = Color(0xFF8B0000), // Dark red
                                    unfocusedLabelColor = Color(0xFF8B0000) // Dark red
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = state.isCategoryDropdownExpanded,
                                onDismissRequest = { viewModel.onEvent(AddExpenseEvent.OnCategoryDropdownExpandedChange(false)) }
                            ) {
                                ExpenseCategory.values().forEach { category ->
                                    DropdownMenuItem(
                                        text = { 
                                            Text(
                                                category.name,
                                                color = Color(0xFF8B0000) // Dark red
                                            ) 
                                        },
                                        onClick = {
                                            viewModel.onEvent(AddExpenseEvent.OnCategorySelect(category))
                                            viewModel.onEvent(AddExpenseEvent.OnCategoryDropdownExpandedChange(false))
                                        }
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = { viewModel.onEvent(AddExpenseEvent.OnSubmit) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFDC143C) // Crimson red
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Add Expense",
                                color = Color(0xFFFFF8DC), // Cornsilk
                                fontWeight = FontWeight.Bold
                            )
                        }

                        state.error?.let { error ->
                            Text(
                                text = error,
                                color = Color(0xFF8B0000), // Dark red
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
} 