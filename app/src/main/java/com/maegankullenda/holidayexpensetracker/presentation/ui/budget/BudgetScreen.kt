package com.maegankullenda.holidayexpensetracker.presentation.ui.budget

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddExpense: () -> Unit,
    onNavigateToExpenseHistory: () -> Unit,
    onNavigateToDailySpendSummary: () -> Unit,
    viewModel: BudgetViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Budget Overview") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BudgetCard(
                title = "Total Budget",
                amount = state.totalBudget,
                currency = state.currency,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            BudgetCard(
                title = "Total Spent",
                amount = state.spentAmount,
                currency = state.currency,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            BudgetCard(
                title = "Remaining Budget",
                amount = state.remainingAmount,
                currency = state.currency,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            BudgetCard(
                title = "Today's Spend",
                amount = state.dailySpend,
                currency = state.currency,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onNavigateToAddExpense
                ) {
                    Text("Add Expense")
                }
                
                Button(
                    onClick = onNavigateToExpenseHistory
                ) {
                    Text("View History")
                }
                
                Button(
                    onClick = onNavigateToDailySpendSummary
                ) {
                    Text("Daily Summary")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetCard(
    title: String,
    amount: Double,
    currency: Currency,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = formatCurrency(amount, currency),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

private fun formatCurrency(amount: Double, currency: Currency): String {
    return when (currency) {
        Currency.ZAR -> "R%.2f".format(amount)
        Currency.USD -> "$%.2f".format(amount)
        Currency.EUR -> "€%.2f".format(amount)
        Currency.GBP -> "£%.2f".format(amount)
        Currency.AUD -> "A$%.2f".format(amount)
        Currency.CAD -> "C$%.2f".format(amount)
    }
} 