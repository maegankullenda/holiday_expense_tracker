package com.maegankullenda.holidayexpensetracker.presentation.ui.expense

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    LaunchedEffect(state.isExpenseAdded) {
        if (state.isExpenseAdded) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.amount,
                onValueChange = { viewModel.onEvent(AddExpenseEvent.OnAmountChange(it)) },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onEvent(AddExpenseEvent.OnDescriptionChange(it)) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = state.isCategoryDropdownExpanded,
                onExpandedChange = { viewModel.onEvent(AddExpenseEvent.OnCategoryDropdownExpandedChange(it)) }
            ) {
                OutlinedTextField(
                    value = state.selectedCategory.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isCategoryDropdownExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = state.isCategoryDropdownExpanded,
                    onDismissRequest = { viewModel.onEvent(AddExpenseEvent.OnCategoryDropdownExpandedChange(false)) }
                ) {
                    ExpenseCategory.values().forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Expense")
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