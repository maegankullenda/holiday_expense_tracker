package com.maegankullenda.holidayexpensetracker.presentation.ui.budget

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maegankullenda.holidayexpensetracker.domain.model.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailySpendSummaryScreen(
    navController: NavController,
    viewModel: BudgetViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Spend Summary") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Today's Spending",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Spent Today",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = when (state.currency) {
                            Currency.ZAR -> "R%.2f".format(state.dailySpend)
                            Currency.USD -> "$%.2f".format(state.dailySpend)
                            Currency.GBP -> "£%.2f".format(state.dailySpend)
                            Currency.EUR -> "€%.2f".format(state.dailySpend)
                            Currency.AUD -> "A$%.2f".format(state.dailySpend)
                            Currency.CAD -> "C$%.2f".format(state.dailySpend)
                        },
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Today's Budget Status",
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Today's Allocation",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = when (state.currency) {
                            Currency.ZAR -> "R%.2f".format(state.remainingAmount / state.daysRemaining)
                            Currency.USD -> "$%.2f".format(state.remainingAmount / state.daysRemaining)
                            Currency.GBP -> "£%.2f".format(state.remainingAmount / state.daysRemaining)
                            Currency.EUR -> "€%.2f".format(state.remainingAmount / state.daysRemaining)
                            Currency.AUD -> "A$%.2f".format(state.remainingAmount / state.daysRemaining)
                            Currency.CAD -> "C$%.2f".format(state.remainingAmount / state.daysRemaining)
                        },
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LinearProgressIndicator(
                progress = (state.dailySpend / (state.remainingAmount / state.daysRemaining)).toFloat().coerceIn(0f, 1f),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "%.1f%% of today's allocation used".format(
                    (state.dailySpend / (state.remainingAmount / state.daysRemaining) * 100).coerceIn(0.0, 100.0)
                ),
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "${state.daysRemaining} days remaining",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
} 