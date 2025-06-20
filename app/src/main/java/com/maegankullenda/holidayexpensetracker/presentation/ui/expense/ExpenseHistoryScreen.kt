package com.maegankullenda.holidayexpensetracker.presentation.ui.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ExpenseGroup(
    val date: LocalDate,
    val totalAmount: Double,
    val expenses: List<Expense>,
    val currency: com.maegankullenda.holidayexpensetracker.domain.model.Currency
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseHistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: ExpenseHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var expandedDates by remember { mutableStateOf(setOf<LocalDate>()) }

    // Thai theme gradient background
    val thaiGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4A90E2), // Thai blue (temple)
            Color(0xFF9B59B6), // Purple (royal)
            Color(0xFFE91E63), // Pink (lotus flower)
            Color(0xFFFF9800), // Orange (saffron)
            Color(0xFFFFD700)  // Gold (Thai gold)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Expense History",
                        color = Color(0xFFFFF8DC), // Cream (Thai silk)
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back",
                            tint = Color(0xFFFFF8DC) // Cream (Thai silk)
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
                .background(thaiGradient)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFFFD700) // Gold
                    )
                }
            } else if (state.expenses.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF8DC) // Cream (Thai silk)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "No expenses yet",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFF4A90E2), // Thai blue
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            } else {
                val expenseGroups = state.expenses
                    .groupBy { it.date }
                    .map { (date, expenses) ->
                        ExpenseGroup(
                            date = date,
                            totalAmount = expenses.sumOf { it.amount },
                            expenses = expenses.sortedByDescending { it.createdAt },
                            currency = expenses.first().currency
                        )
                    }
                    .sortedByDescending { it.date }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(expenseGroups) { group ->
                        ExpenseGroupItem(
                            expenseGroup = group,
                            isExpanded = expandedDates.contains(group.date),
                            onToggleExpanded = { date ->
                                expandedDates = if (expandedDates.contains(date)) {
                                    expandedDates - date
                                } else {
                                    expandedDates + date
                                }
                            },
                            onDeleteExpense = { expense ->
                                viewModel.onEvent(ExpenseHistoryEvent.DeleteExpense(expense))
                            }
                        )
                    }
                }
            }
        }

        if (state.showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.onEvent(ExpenseHistoryEvent.DismissDeleteDialog)
                },
                title = { 
                    Text(
                        "Delete Expense",
                        color = Color(0xFF4A90E2) // Thai blue
                    ) 
                },
                text = {
                    Text(
                        "Are you sure you want to delete this expense? " +
                        "This action cannot be undone.",
                        color = Color(0xFF2C3E50) // Dark blue-gray
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.onEvent(ExpenseHistoryEvent.ConfirmDelete)
                        }
                    ) {
                        Text(
                            "Delete", 
                            color = Color(0xFFE91E63) // Pink (lotus)
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.onEvent(ExpenseHistoryEvent.DismissDeleteDialog)
                        }
                    ) {
                        Text(
                            "Cancel",
                            color = Color(0xFF4A90E2) // Thai blue
                        )
                    }
                },
                containerColor = Color(0xFFFFF8DC), // Cream (Thai silk)
                shape = RoundedCornerShape(16.dp)
            )
        }

        state.error?.let { error ->
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF8DC) // Cream (Thai silk)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = error,
                    color = Color(0xFFE91E63), // Pink (lotus)
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseGroupItem(
    expenseGroup: ExpenseGroup,
    isExpanded: Boolean,
    onToggleExpanded: (LocalDate) -> Unit,
    onDeleteExpense: (Expense) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8DC) // Cream (Thai silk)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with date, total amount, and expand/collapse button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = expenseGroup.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A90E2) // Thai blue
                    )
                    
                    Text(
                        text = "Total: ${formatCurrency(expenseGroup.totalAmount, expenseGroup.currency)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF9B59B6) // Purple (royal)
                    )
                }

                IconButton(
                    onClick = { onToggleExpanded(expenseGroup.date) }
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = Color(0xFF4A90E2) // Thai blue
                    )
                }
            }

            // Expanded content showing individual expenses
            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                
                expenseGroup.expenses.forEach { expense ->
                    ExpenseItem(
                        expense = expense,
                        onDelete = { onDeleteExpense(expense) },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseItem(
    expense: Expense,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F8FF) // Light blue (Thai temple)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = expense.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C3E50) // Dark blue-gray
                )
                
                Text(
                    text = expense.category.name.lowercase().capitalize(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9B59B6) // Purple (royal)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = formatCurrency(expense.amount, expense.currency),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF9800) // Orange (saffron)
                )

                IconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color(0xFFE91E63) // Pink (lotus)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete expense",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

private fun formatCurrency(amount: Double, currency: com.maegankullenda.holidayexpensetracker.domain.model.Currency): String {
    return when (currency) {
        com.maegankullenda.holidayexpensetracker.domain.model.Currency.ZAR -> "R%.2f".format(amount)
        com.maegankullenda.holidayexpensetracker.domain.model.Currency.USD -> "$%.2f".format(amount)
        com.maegankullenda.holidayexpensetracker.domain.model.Currency.GBP -> "£%.2f".format(amount)
        com.maegankullenda.holidayexpensetracker.domain.model.Currency.EUR -> "€%.2f".format(amount)
        com.maegankullenda.holidayexpensetracker.domain.model.Currency.AUD -> "A$%.2f".format(amount)
        com.maegankullenda.holidayexpensetracker.domain.model.Currency.CAD -> "C$%.2f".format(amount)
    }
}

private fun String.capitalize(): String {
    return if (isNotEmpty()) {
        this[0].uppercase() + substring(1)
    } else {
        this
    }
} 